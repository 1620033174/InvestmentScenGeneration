use std::path;

use actix_web::{get, web, Responder};
use anyhow::Result;
use serde_json::json;

use crate::{
    app::{generate_headers},
    error::{self, UserError},
    python::{pyo3_runtime::Pyo3Runtime, PythonRuntime},
};

use super::ok_response;

#[get("/api/klines/{stock_id}/{time}")]
async fn klines(
    path: web::Path<(String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (stock_id, time) = path.into_inner();
    info!("/api/klines/{stock_id}/{time}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    } else if time.is_empty() {
        return Err(UserError::ValidationError {
            field: "time".to_string(),
        });
    }

    let stock = json!({"stock_id": stock_id, "_quarter": time});
    let json = serde_json::to_string(&stock).unwrap();

    let runtime = Pyo3Runtime::global();
    let path = path::Path::new("algorithm/klines/klines.py");
    let result = runtime.call1(path, "get_result", json).map_err(|e| {
        error!("{:?}", e);
        UserError::InternalError
    })?;
    let result = result.as_array().unwrap().clone();
    if result.is_empty() {
        return Err(UserError::EmptyError);
    }
    let headers = generate_headers(result.clone()).unwrap();
    Ok(web::Json(ok_response(headers, result)))
}
