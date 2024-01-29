use std::path;

use actix_web::{get, web, Responder};
use anyhow::Result;



use serde_json::Value;

use crate::{
    app::get_stock_by_id_and_time,
    error::{self, UserError},
    python::{pyo3_runtime::Pyo3Runtime, PythonRuntime},
    AppState,
};

use super::ok_response;

#[get("/api/most_similar/{stock_id}/{time}")]
async fn most_similar(
    db: web::Data<AppState>,
    path: web::Path<(String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (stock_id, time) = path.into_inner();
    info!("/api/most_similar/{stock_id}/{time}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    } else if time.is_empty() {
        return Err(UserError::ValidationError {
            field: "time".to_string(),
        });
    }
    let stock = get_stock_by_id_and_time(db, &stock_id, &time).await?;
    if stock.is_none() {
        return Err(UserError::EmptyError);
    }

    let stock = stock.unwrap();
    let json = serde_json::to_string(&stock).unwrap();

    let runtime = Pyo3Runtime::global();
    let path = path::Path::new("algorithm/most-similar/most-similar.py");
    let result = runtime.call1(path, "get_result", json).map_err(|e| {
        error!("{:?}", e);
        UserError::InternalError
    })?;

    Ok(web::Json(ok_response(Value::Null, result)))
}
