use std::path;

use actix_web::{get, web, Responder};
use anyhow::Result;

use serde_json::Value;

use crate::{
    error::{self, UserError},
    python::{pyo3_runtime::Pyo3Runtime, PythonRuntime},
};

use super::ok_response;

#[get("/api/about/{stock_id}")]
async fn about(path: web::Path<String>) -> Result<impl Responder, error::UserError> {
    let stock_id = path.into_inner();
    info!("/api/about/{stock_id}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    }

    let runtime = Pyo3Runtime::global();
    let path = path::Path::new("algorithm/about/about.py");

    let result = runtime.call1(path, "get_result", stock_id).map_err(|e| {
        error!("{:?}", e);
        UserError::InternalError
    })?;
    Ok(web::Json(ok_response(Value::Null, result)))
}
