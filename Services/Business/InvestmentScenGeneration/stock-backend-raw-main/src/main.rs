mod algorithm;
mod app;
mod error;
mod init;
mod python;
mod utils;
mod watch;

use std::{collections::HashMap, path::PathBuf, process::Child, sync::Mutex};

use actix_web::{middleware::Logger, App, HttpServer};
use env_logger::{Builder, Target};
use init::init_data;
use mongodb::{bson::doc, Database};
use once_cell::sync::{Lazy, OnceCell};
use python::pyo3_runtime::Pyo3Runtime;
use serde::Serialize;

use crate::init::{run_cron_algorithm, init_index};

#[macro_use]
extern crate log;

#[derive(Debug, serde::Serialize, serde::Deserialize)]
struct WebResponse<T: Serialize, U: Serialize> {
    status: u16,
    err: String,
    headers: T,
    data: U,
}

struct AppState {
    db: Database,
}

static RUNTIME: OnceCell<Pyo3Runtime> = OnceCell::new();
static PATH_PID_MAP: Lazy<Mutex<HashMap<PathBuf, Child>>> = Lazy::new(|| {
    let m = HashMap::new();
    Mutex::new(m)
});

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    // logger
    if let Err(_e) = std::env::var("RUST_LOG") {
        std::env::set_var("RUST_LOG", "INFO")
    }
    // 设置时区为 Asia/Shanghai
    std::env::set_var("TZ", "Asia/Shanghai");
    let mut builder = Builder::from_default_env();
    builder.target(Target::Stdout);
    builder.init();

    // python runtime
    let runtime = Pyo3Runtime::new();
    RUNTIME.set(runtime).unwrap();

    // database
    let db = init::init_mongo_client().await;
    init_data(&db).await;

    // watcher
    let _watcher = init::init_watch(&db).await.expect("初始化文件变化监听失败");
    info!("文件变化监听已启动");

    // algorithm-cron
    run_cron_algorithm();

    let current = std::env::current_dir().expect("获取当前路径失败");
    info!("当前工作路径: {:?}", current);

    // http server
    HttpServer::new(move || {
        let logger = Logger::default();
        App::new()
            .wrap(logger)
            .app_data(actix_web::web::Data::new(AppState { db: db.clone() }))
            .service(app::basic::get_stocks_with_quarter)
            .service(app::basic::get_filters)
            .service(app::factor::compare_stock_with_time)
            .service(app::factor::query_factor_with_time)
            .service(app::similar::most_similar)
            .service(app::history::history_time)
            .service(app::about::about)
            .service(app::klines::klines)
            .service(app::related::related)
            .service(app::unrelated::unrelated)
            .service(app::basic::query_all)
            .service(app::basic::query_stock)
            .service(app::basic::query_stock_with_time)
            .service(app::title::update_title)
    })
    .bind(("0.0.0.0", 8000))
    .expect("8000端口被占用，启动服务器失败")
    .run()
    .await
}