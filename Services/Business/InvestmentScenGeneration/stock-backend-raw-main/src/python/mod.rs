use std::path::Path;

use pyo3::{FromPyObject};

pub mod pyo3_runtime;

pub trait PythonRuntime {
    // 参数为脚本路径
    // 需要保证 Python 脚本有 get_result 方法且返回 dataframe
    fn get_python_dataframe(&self, path: &Path) -> anyhow::Result<serde_json::Value>;

    // 参数为脚本路径
    // 这里不设置工作路径
    fn get_python_dataframe_unset_path(&self, path: &std::path::Path) -> anyhow::Result<serde_json::Value>;

    // 带参数的调用
    // method 为方法名，param 为参数
    fn call1(&self, path: &Path, method: &str, param: String) -> anyhow::Result<serde_json::Value>;

    fn call1_as_f64(
        &self,
        path: &std::path::Path,
        method: &str,
        param: String,
    ) -> anyhow::Result<f64>;

    fn call1_with_type<T: FromPyObject<'static> + 'static>(
        &self,
        path: &std::path::Path,
        method: &str,
        param: String,
    ) -> anyhow::Result<T>;

}
