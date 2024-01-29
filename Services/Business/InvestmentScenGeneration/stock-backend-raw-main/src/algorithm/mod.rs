use crate::python::PythonRuntime;
use crate::utils;

use serde_json::Value;
use std::collections::HashMap;
use std::fmt::Debug;
use std::path::Path;
use std::process::Command;

// 初始化的时候被调用，需要特殊处理的文件夹逻辑可以写在这里
// 返回文件夹名和 dataframe 数据
pub fn run_all_algorithm<R: PythonRuntime>(runtime: &R) -> anyhow::Result<HashMap<String, Value>> {
    let dirs =
        utils::get_contents_of_dir("python-src").expect("Python 脚本文件夹 python-src 不存在");
    let mut result_map: HashMap<String, Value> = HashMap::new();
    for dir in dirs {
        let dir_clone = dir.clone();
        let files = utils::get_contents_of_dir(dir).unwrap();
        for file in files {
            if file.as_path().file_stem() == dir_clone.as_path().file_stem()
                && file.extension().unwrap() == "py"
            {
                info!("running sciript: {:?}", file);
                let result = run_algorithm(runtime, &file).unwrap();
                result_map.insert(
                    file.as_path()
                        .file_stem()
                        .unwrap()
                        .to_str()
                        .unwrap()
                        .to_string(),
                    result,
                );
            }
        }
    }
    Ok(result_map)
}

// 不设置工作路径
pub fn run_dir<P>(dir: P) -> anyhow::Result<std::process::Child>
where
    P: AsRef<Path> + Debug,
{
    assert!(dir.as_ref().is_dir());
    let files = utils::get_contents_of_dir(&dir).unwrap();
    let dir_clone = dir.as_ref();
    info!("命令运行算法文件夹: {dir_clone:?}");
    for file in files {
        if file.as_path().file_stem() == dir_clone.file_stem() && file.extension().unwrap() == "py"
        {
            info!("运行脚本: {:?}", file);
            return Ok(run_python_with_command(&file)?);
        }
    }
    Err(anyhow::anyhow!("{dir:?} 目录无可执行 python 脚本，请确认 python 脚本和文件夹名相同"))
}

fn run_python_with_command(path: &Path) -> Result<std::process::Child, std::io::Error> {
    Command::new("python3").arg(path).spawn()
}

// 重启的时候增量更新和热更新的时候被调用，参数是可运行的文件夹路径
// 返回 dataframe
pub fn run_algorithm_dir<R, P>(runtime: &R, dir: P) -> anyhow::Result<serde_json::Value>
where
    R: PythonRuntime,
    P: AsRef<Path>,
{
    assert!(dir.as_ref().is_dir());
    let files = utils::get_contents_of_dir(&dir).unwrap();
    let dir_clone = dir.as_ref();
    info!("运行算法文件夹: {dir_clone:?}");
    for file in files {
        if file.as_path().file_stem() == dir_clone.file_stem() && file.extension().unwrap() == "py"
        {
            info!("running sciript: {:?}", file);
            return run_algorithm(runtime, &file);
        }
    }
    Ok(serde_json::Value::Null)
}

// path 是 python 文件路径
// pub fn run_algorithm_unset_path<R, P>(runtime: &R, path: P) -> anyhow::Result<serde_json::Value>
// where
//     R: PythonRuntime,
//     P: AsRef<Path>,
// {
//     runtime.get_python_dataframe_unset_path(path.as_ref())
// }

// path 是 python 文件路径
pub fn run_algorithm<R, P>(runtime: &R, path: P) -> anyhow::Result<serde_json::Value>
where
    R: PythonRuntime,
    P: AsRef<Path>,
{
    runtime.get_python_dataframe(path.as_ref())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_run_algorithm() {
        let result = run_all_algorithm(&crate::python::pyo3_runtime::Pyo3Runtime).unwrap();
        println!("{result:?}");
    }
}
