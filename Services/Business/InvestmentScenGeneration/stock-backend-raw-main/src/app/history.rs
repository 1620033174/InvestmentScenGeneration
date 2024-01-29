use std::path;

use actix_web::{get, web, Responder};
use anyhow::Result;





use crate::{
    error::{self, UserError},
    python::{pyo3_runtime::Pyo3Runtime, PythonRuntime}, app::generate_headers,
};

use super::ok_response;

#[get("/api/historyTime/{stock_id}")]
async fn history_time(
    path: web::Path<String>,
) -> Result<impl Responder, error::UserError> {
    let stock_id = path.into_inner();
    info!("/api/historyTime/{stock_id}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    }

    let runtime = Pyo3Runtime::global();
    let path = path::Path::new("algorithm/historyTime/historyTime.py");

    let result = runtime.call1(path, "get_result", stock_id).map_err(|e| {
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
