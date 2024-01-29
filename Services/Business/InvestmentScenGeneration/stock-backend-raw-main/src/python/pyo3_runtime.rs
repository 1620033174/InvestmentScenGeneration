use crate::RUNTIME;

use super::PythonRuntime;
use anyhow::{Ok, Result};

use pyo3::{prelude::*, types::IntoPyDict};
use std::io::Read;

#[derive(Debug)]
pub struct Pyo3Runtime;

impl Pyo3Runtime {
    pub fn global() -> &'static Pyo3Runtime {
        RUNTIME.get().expect("python runtime is not initialized")
    }

    pub fn new() -> Pyo3Runtime {
        Pyo3Runtime
    }
}

impl PythonRuntime for Pyo3Runtime {
    // 通过 CPython 调用脚本文件以支持 pandas 等库
    // 参数为脚本路径
    // 需要保证 Python 脚本有 get_result 方法且返回 dataframe
    fn get_python_dataframe(&self, path: &std::path::Path) -> anyhow::Result<serde_json::Value> {
        let mut file = std::fs::File::open(path).expect("文件不存在");
        let mut codes = String::new();
        file.read_to_string(&mut codes)
            .expect("无法读取 python 脚本文件");
        // // 设置 Python 脚本工作路径，保证脚本能够读数据
        // let current = env::current_dir()?;
        // env::set_current_dir(path.parent().unwrap())?;
        // info!("current dir: {:?}", current);
        let from_python = Python::with_gil(|py| -> Result<String> {
            let algorithm = PyModule::from_code(py, &codes, "", "");
            let is_dataframe = algorithm?
                .getattr("get_result")?
                .call0()?
                .getattr("to_json")
                .is_ok();
            let algorithm = PyModule::from_code(py, &codes, "", "");
            let app = {
                if is_dataframe {
                    // 将 Dataframe 转换为 json
                    let kwargs = [("orient", "records")].into_py_dict(py);
                    algorithm?
                        .getattr("get_result")?
                        .call0()?
                        .getattr("to_json")?
                        .call((), Some(kwargs))?
                        .extract::<String>()?
                } else {
                    let dict = algorithm?
                        .getattr("get_result")?
                        .call0()?
                        .extract::<PyObject>()
                        .expect("反序列化 Python 数据失败");
                    let args = (dict,);
                    let json = PyModule::import(py, "json")?;
                    json.getattr("dumps")?.call1(args)?.extract::<String>()?
                }
            };
            Ok(app)
        });
        let origin_data = from_python?;
        let result = serde_json::from_str(&origin_data).expect("解析python数据失败");
        Ok(result)
    }

    fn get_python_dataframe_unset_path(
        &self,
        path: &std::path::Path,
    ) -> anyhow::Result<serde_json::Value> {
        let mut file = std::fs::File::open(path).expect("文件不存在");
        let mut codes = String::new();
        file.read_to_string(&mut codes)
            .expect("无法读取 python 脚本文件");
        Python::with_gil(|py| {
            let algorithm = PyModule::from_code(py, &codes, "", "");
            algorithm
                .expect("获取定时任务失败")
                .getattr("get_result")
                .expect("定时任务无 get_result() 方法")
                .call0()
                .expect("调用定时任务 get_result() 方法失败");
        });
        Ok(serde_json::Value::Null)
    }

    // 通过 CPython 调用 Python 脚本
    fn call1(
        &self,
        path: &std::path::Path,
        method: &str,
        param: String,
    ) -> anyhow::Result<serde_json::Value> {
        let mut file = std::fs::File::open(path).expect("文件不存在");
        let mut codes = String::new();
        file.read_to_string(&mut codes)
            .expect("无法读取 python 脚本文件");
        // 设置 Python 脚本工作路径，保证脚本能够读数据
        // let current = env::current_dir()?;
        // env::set_current_dir(path.parent().unwrap())?;
        // info!("current dir: {:?}", path.parent());

        // 调用 Python 脚本
        let from_python = Python::with_gil(|py| -> Result<String> {
            let algorithm = PyModule::from_code(py, &codes, "", "");
            let param = (param,);
            let is_dataframe = algorithm?
                .getattr(method)?
                .call1(param.clone())?
                .getattr("to_json")
                .is_ok();
            let algorithm = PyModule::from_code(py, &codes, "", "");
            let app = {
                if is_dataframe {
                    // 将 Dataframe 转换为 json
                    let kwargs = [("orient", "records")].into_py_dict(py);
                    algorithm?
                        .getattr(method)?
                        .call1(param)?
                        .getattr("to_json")?
                        .call((), Some(kwargs))?
                        .extract::<String>()?
                } else {
                    // json
                    let dict = algorithm?
                        .getattr(method)?
                        .call1(param)?
                        .extract::<PyObject>()
                        .expect("反序列化 Python 数据失败");
                    let args = (dict,);
                    let json = PyModule::import(py, "json")?;
                    json.getattr("dumps")?.call1(args)?.extract::<String>()?
                }
            };
            Ok(app)
        });

        // 恢复之前的工作路径
        // env::set_current_dir(current)?;
        let origin_data = from_python?;
        let result = serde_json::from_str(&origin_data).expect("解析python数据失败");
        Ok(result)
    }

    fn call1_as_f64(
        &self,
        path: &std::path::Path,
        method: &str,
        param: String,
    ) -> anyhow::Result<f64> {
        let mut file = std::fs::File::open(path).expect("文件不存在");
        let mut codes = String::new();
        file.read_to_string(&mut codes)
            .expect("无法读取 python 脚本文件");
        // 设置 Python 脚本工作路径，保证脚本能够读数据
        // let current = env::current_dir()?;
        // env::set_current_dir(path.parent().unwrap())?;
        // info!("current dir: {:?}", path.parent());

        // 调用 Python 脚本
        let from_python = Python::with_gil(|py| -> Result<f64> {
            let algorithm = PyModule::from_code(py, &codes, "", "");
            let param = (param,);
            let app = algorithm?.getattr(method)?.call1(param)?.extract::<f64>()?;
            Ok(app)
        });

        // 恢复之前的工作路径
        // env::set_current_dir(current)?;
        let result = from_python?;
        Ok(result)
    }

    fn call1_with_type<T: FromPyObject<'static> + 'static>(
        &self,
        _path: &std::path::Path,
        _method: &str,
        _param: String,
    ) -> anyhow::Result<T> {
        todo!();
        //     let mut file = std::fs::File::open(path).expect("文件不存在");
        //     let mut codes = String::new();
        //     file.read_to_string(&mut codes)
        //         .expect("无法读取 python 脚本文件");
        //     // 设置 Python 脚本工作路径，保证脚本能够读数据
        //     let current = env::current_dir()?;
        //     env::set_current_dir(path.parent().unwrap())?;
        //     info!("current dir: {:?}", path.parent());

        //     // 调用 Python 脚本
        //     let from_python = Python::with_gil(|py| {
        //         // 将 py 参数转换为 Py<Python> 类型的对象
        //         let algorithm = PyModule::from_code(py, &codes, "", "");
        //         let param = (param,);
        //         let app = algorithm?
        //             .getattr(method)?
        //             .call1(param.clone())?
        //             .extract::<T>()?;
        //         Ok(app)
        //     });

        //     // 恢复之前的工作路径
        //     env::set_current_dir(current)?;
        //     from_python
    }
}
