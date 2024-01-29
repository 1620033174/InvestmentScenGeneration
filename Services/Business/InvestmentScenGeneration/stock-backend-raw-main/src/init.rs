use mongodb::{bson::doc, options::ClientOptions, Client, Database, IndexModel};
use notify::{Error, Event, RecommendedWatcher, RecursiveMode, Watcher};
use serde_json::{json, Value};
use std::{
    collections::{HashMap, HashSet},
    path::Path,
    sync::Mutex,
    time::Duration,
};
use tokio::sync::mpsc::Receiver;

use crate::{
    algorithm::{run_algorithm_dir, run_all_algorithm, run_dir},
    python::pyo3_runtime::Pyo3Runtime,
    utils::get_contents_of_dir,
    watch::{self, Updater},
    PATH_PID_MAP,
};

static DB_NAME: &str = "stock_db";

pub async fn init_mongo_client() -> Database {
    info!("正在初始化数据库连接");
    let conn = std::env::var("CONN").unwrap_or("mongodb://127.0.0.1:27017".to_string());
    let mut client_options = ClientOptions::parse(conn)
        .await
        .expect("解析数据库链接失败");
    client_options.app_name = Some("stock".to_string());
    let client = Client::with_options(client_options).expect("创建数据库连接失败");

    client.database(DB_NAME)
}

pub fn file_len(path: &str) -> u64 {
    use std::fs;
    let metadata = fs::metadata(path);
    match metadata {
        Ok(m) => return m.len(),
        Err(_) => return 0,
    }
}

// 初始化的时候调用，Config.toml 不存在的时候初始化所有数据
// Config.toml 存在的时候进行增量更新
pub async fn init_data(db: &Database) {
    if !std::path::Path::new("Config.toml").exists() || file_len("Config.toml") == 0 {
        drop_all_data(db).await;
        init_all_data(db).await;
        init_index(db).await;
        info!("初始化完成");
    } else {
        info!("已完成数据库初始化");
        info!("正在检测数据集/算法变化…");
        let mut toml_config = watch::toml::TomlConfig {
            files: Mutex::new(HashMap::new()),
        };
        let files = toml_config
            .changed_files(Path::new("python-src"))
            .expect("获取数据集/算法变化失败");
        // let cron_files = toml_config
        //     .changed_files(Path::new("cron-algorithm"))
        //     .expect("获取数据集/算法变化失败");
        let mut files_set: HashSet<&Path> = HashSet::new();
        // let mut cron_files_set: HashSet<&Path> = HashSet::new();
        for file in &files {
            let path = Path::new(file);
            let parent_path = path.parent().unwrap();
            files_set.insert(parent_path);
        }
        // for cron_file in &cron_files {
        //     let path = Path::new(cron_file);
        //     let parent_path = path.parent().unwrap();
        //     cron_files_set.insert(parent_path);
        // }
        info!("发生变化的文件夹：{files_set:?}");
        // info!("发生变化的文件夹：{cron_files_set:?}");
        let runtime = Pyo3Runtime::global();
        for dir in files_set {
            let collection_name = dir.file_stem().unwrap().to_str().unwrap();
            let v = run_algorithm_dir(runtime, dir).expect("运行算法文件夹失败");
            update_value_data_if_exist(db, collection_name, &v).await;
        }
        // for dir in cron_files_set {
        //     let _ = run_algorithm_dir(runtime, dir).expect("运行算法文件夹失败");
        // }
        info!("数据集/算法变动更新完毕，启动 HTTP Server");
        toml_config.save_files();
    }
    rust_malloc_trim();
}

// stocks 表和 factor 表需要经常进行查询
pub async fn init_index(db: &Database) {
    let stocks_index = IndexModel::builder()
        .keys(doc! { "stock_id": 1, "_quarter": 1, "_selected_time": 1 })
        .build();
    db.collection::<Value>("stocks")
        .create_index(stocks_index, None)
        .await
        .expect("stocks 创建索引失败");

    let factor_index = IndexModel::builder()
        .keys(doc! {"stock_id": 1, "_quarter": 1, "_time": 1 })
        .build();
    db.collection::<Value>("factors")
        .create_index(factor_index, None)
        .await
        .expect("factors 创建索引失败");

    info!("创建索引成功");
}


pub async fn update_value_data_if_exist<N: AsRef<str>>(
    db: &Database,
    collection_name: N,
    data: &Value,
) {
    // let _ = db
    //     .collection::<Value>(collection_name.as_ref())
    //     .count_documents(None, None)
    //     .await
    //     .expect("获取表数据条数失败");
    // 改为增量更新，不删除表数据
    // if count > 0 {
    //     drop_collection(db, collection_name.as_ref()).await;
    // }
    insert_value_data(db, collection_name.as_ref(), data).await;
}

async fn insert_value_data<N: AsRef<str>>(db: &Database, collection_name: N, data: &Value) {
    let collection = db.collection::<Value>(collection_name.as_ref());
    if data.is_array() {
        let insert_data = data
            .as_array()
            .expect("数据无法转换为数组，请确认数据是否正确");
        collection
            .insert_many(insert_data, None)
            .await
            .expect("插入数组数据失败");
    } else {
        collection
            .insert_one(data, None)
            .await
            .expect("插入对象数据失败");
    }

    info!("插入数据成功");
}

fn rust_malloc_trim() {
    unsafe {
        libc::malloc(0);
    }
    info!("libc malloc trimed");
}

async fn init_all_data(db: &Database) {
    init_title_data(db).await;
    let runtime = Pyo3Runtime;
    let result = run_all_algorithm(&runtime).expect("运行 Python 脚本失败");
    for (path, value) in &result {
        let name = Path::new(&path).file_stem().unwrap().to_str().unwrap();
        insert_value_data(db, name, value).await;
        info!("{:?}", name);
    }
    drop(result);
    info!("dropped json map");
    let mut toml_config = watch::toml::TomlConfig {
        files: Mutex::new(HashMap::new()),
    };
    toml_config.init_files(Path::new("python-src"));
}

async fn init_title_data(db: &Database) {
    let value = json!({"title": "高送转预期投资场景", "describe": "“高送转”是高送转股票的简称，指送股或转增股比例较高的一些股票。由于高送转股在送转公告发布前通常股价涨势强劲，能够使投资者短期内获得较高的投资收益，“高送转”也成为了半年报和年报出台前常见的炒作题材。 高送转发生的前提条件：公积金高、未分配利润高、净资产高；公司的公积金用于公司的亏损、扩大公司生产经营或者转为增加公司资本；所以，只有每股资本公积金高公司才具备高送转的条件。"});
    insert_value_data(db, "title", &value).await;
}

// async fn drop_collection<N: AsRef<str>>(db: &Database, collection_name: N) {
//     db.collection::<Value>(collection_name.as_ref())
//         .drop(None)
//         .await
//         .expect("删除表失败");
// }

async fn drop_all_data(db: &Database) {
    db.drop(None).await.expect("删除数据库失败");
    info!("删除数据库成功");
}

// 这里使用 inotify 来监听文件夹变化情况
#[cfg(feature = "poll")]
fn init_watcher() -> notify::Result<(notify::PollWatcher, Receiver<Event>)> {
    let (tx, rx) = tokio::sync::mpsc::channel::<Event>(10);

    let watcher = notify::PollWatcher::new(
        move |res: Result<Event, Error>| match res {
            Ok(event) => {
                std::thread::sleep(Duration::from_secs(3));
                if event.kind.is_modify() {
                    tx.blocking_send(event).unwrap();
                }
            }
            Err(e) => error!("watch error: {:?}", e),
        },
        notify::Config::default().with_poll_interval(Duration::from_secs(2)),
    )?;

    Ok((watcher, rx))
}

#[cfg(feature = "poll")]
pub async fn init_watch(db: &Database) -> notify::Result<notify::PollWatcher> {
    let (mut watcher, mut rx) = init_watcher()?;
    info!("创建 poll watcher 成功");
    watcher.watch(Path::new("python-src/stocks"), RecursiveMode::Recursive)?;
    watcher.watch(Path::new("cron-algorithm"), RecursiveMode::Recursive)?;
    let db = db.clone();

    // 这里是检测到文件夹修改之后的处理逻辑，特殊文件夹的处理逻辑写在这里
    actix_web::rt::spawn(async move {
        let runtime = Pyo3Runtime::global();
        while let Some(event) = rx.recv().await {
            info!("changed event: {event:?}");

            if !event.paths.is_empty()
                && event.paths[0]
                    .as_path()
                    .parent()
                    .unwrap()
                    .parent()
                    .unwrap()
                    .file_stem()
                    .is_some()
                && event.paths[0]
                    .as_path()
                    .parent()
                    .unwrap()
                    .parent()
                    .unwrap()
                    .file_stem()
                    .unwrap()
                    == "cron-algorithm"
            {
                info!("定时任务被更新");
                for path in event.paths {
                    if path.as_path().exists() {
                        let parent = path.parent().unwrap().to_owned();
                        let mut map = PATH_PID_MAP.lock().unwrap();
                        if map.contains_key(&parent) {
                            let mut child = map.remove(&parent).unwrap();
                            let id = child.id();
                            child.kill().unwrap();
                            child.wait().expect("结束子进程失败");
                            info!("定时任务已结束，PID: {id}");
                        }
                        let child = run_dir(&parent).expect("运行定时任务失败");
                        let id = child.id();
                        info!("定时任务已启动，PID: {id}");
                        map.insert(parent, child);
                    }
                }
                continue;
            }

            info!("算法/数据集被改动");
            let mut toml_config = watch::toml::TomlConfig {
                files: Mutex::new(HashMap::new()),
            };
            info!("对比变化中");
            let files = toml_config
                .changed_files(Path::new("python-src/stocks"))
                .expect("获取数据集/算法变化失败");
            let mut files_set: HashSet<&Path> = HashSet::new();
            for file in &files {
                let path = Path::new(file);
                let parent_path = path.parent().unwrap();
                files_set.insert(parent_path);
            }
            if !files_set.is_empty() {
                info!("发生变化的文件夹：{files_set:?}");
                for dir in files_set {
                    let collection_name = dir.file_stem().unwrap().to_str().unwrap();
                    let v = run_algorithm_dir(runtime, dir);
                    match v {
                        Ok(v) => update_value_data_if_exist(&db, collection_name, &v).await,
                        Err(_) => error!("运行算法文件夹{dir:?}失败"),
                    }
                }
                toml_config.save_files();
            } else {
                info!("内容无变化");
            }
        }
    });

    Ok(watcher)
}

// 这里使用 inotify 来监听文件夹变化情况
#[cfg(not(feature = "poll"))]
fn init_watcher() -> notify::Result<(RecommendedWatcher, Receiver<Event>)> {
    let (tx, rx) = tokio::sync::mpsc::channel::<Event>(10);

    let watcher = notify::recommended_watcher(move |res: Result<Event, Error>| match res {
        Ok(event) => {
            std::thread::sleep(Duration::from_secs(3));
            if event.kind.is_modify() {
                tx.blocking_send(event).unwrap();
            }
        }
        Err(e) => error!("watch error: {:?}", e),
    })?;

    Ok((watcher, rx))
}

#[cfg(not(feature = "poll"))]
pub async fn init_watch(db: &Database) -> notify::Result<RecommendedWatcher> {
    let (mut watcher, mut rx) = init_watcher()?;
    info!("创建 inotify watcher 成功");
    watcher.watch(Path::new("python-src/stocks"), RecursiveMode::Recursive)?;
    watcher.watch(Path::new("cron-algorithm"), RecursiveMode::Recursive)?;
    let db = db.clone();

    // 这里是检测到文件夹修改之后的处理逻辑，特殊文件夹的处理逻辑写在这里
    actix_web::rt::spawn(async move {
        let runtime = Pyo3Runtime::global();
        while let Some(event) = rx.recv().await {
            info!("changed event: {event:?}");

            if !event.paths.is_empty()
                && event.paths[0]
                    .as_path()
                    .parent()
                    .unwrap()
                    .parent()
                    .unwrap()
                    .file_stem()
                    .unwrap()
                    == "cron-algorithm"
            {
                info!("定时任务被更新");
                for path in event.paths {
                    if path.as_path().exists() {
                        let parent = path.parent().unwrap().to_owned();
                        let mut map = PATH_PID_MAP.lock().unwrap();
                        if map.contains_key(&parent) {
                            let mut child = map.remove(&parent).unwrap();
                            let id = child.id();
                            child.kill().unwrap();
                            child.wait().expect("结束子进程失败");
                            info!("定时任务已结束，PID: {id}");
                        }
                        let child = run_dir(&parent).expect("运行定时任务失败");
                        let id = child.id();
                        info!("定时任务已启动，PID: {id}");
                        map.insert(parent, child);
                    }
                }
                continue;
            }

            info!("算法/数据集被改动");
            let mut toml_config = watch::toml::TomlConfig {
                files: Mutex::new(HashMap::new()),
            };
            info!("对比变化中");
            let files = toml_config
                .changed_files(Path::new("python-src/stocks"))
                .expect("获取数据集/算法变化失败");
            let mut files_set: HashSet<&Path> = HashSet::new();
            for file in &files {
                let path = Path::new(file);
                let parent_path = path.parent().unwrap();
                files_set.insert(parent_path);
            }
            if !files_set.is_empty() {
                info!("发生变化的文件夹：{files_set:?}");
                for dir in files_set {
                    let collection_name = dir.file_stem().unwrap().to_str().unwrap();
                    let v = run_algorithm_dir(runtime, dir);
                    match v {
                        Ok(v) => update_value_data_if_exist(&db, collection_name, &v).await,
                        Err(_) => error!("运行算法文件夹{dir:?}失败"),
                    }
                }
                toml_config.save_files();
            } else {
                info!("内容无变化");
            }
        }
    });

    Ok(watcher)
}

pub fn run_cron_algorithm() {
    let contents = get_contents_of_dir("cron-algorithm").unwrap();
    info!("定时任务：{contents:?}");
    for content in contents {
        let current = std::env::current_dir().expect("获取当前路径失败");
        info!("当前工作路径: {:?}", current);
        let child = run_dir(content.as_path()).expect("运行定时任务失败");
        let id = child.id();
        info!("定时任务已启动，PID: {id}");
        PATH_PID_MAP.lock().unwrap().insert(content, child);
    }
}
