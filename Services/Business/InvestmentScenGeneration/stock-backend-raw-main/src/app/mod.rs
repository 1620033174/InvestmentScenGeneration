pub mod about;
pub mod basic;
pub mod factor;
pub mod history;
pub mod klines;
pub mod related;
pub mod similar;
pub mod title;
pub mod unrelated;

use actix_web::web;
use mongodb::{bson::doc, options::FindOneOptions};
use serde::Serialize;
use serde_json::Value;

use crate::{error::UserError, AppState, WebResponse};

async fn get_stock_by_id_and_time(
    db: web::Data<AppState>,
    stock_id: &str,
    time: &str,
) -> Result<Option<Value>, UserError> {
    let collection = &db.db.collection::<Value>("stocks");
    let options = FindOneOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    collection
        .find_one(doc! { "stock_id":stock_id, "_quarter": time }, options)
        .await
        .map_err(|_| UserError::InternalError)
}

fn generate_headers(v: Vec<Value>) -> Result<Vec<String>, UserError> {
    if !v.is_empty() {
        let first = v[0].clone();
        let clone = first;
        let mut h: Vec<String> = clone.as_object().unwrap().keys().cloned().collect();
        h.retain(|x| !x.starts_with('_'));
        h.retain(|x| x != "stock_id");
        h.retain(|x| !x.contains("Unnamed"));
        // rotate for factor
        if h.len() == 5 && h[3].starts_with("20") {
            h.rotate_right(1);
        } else if h.len() > 0 && h[0].starts_with("20") { // factor 不够的情况
            h.rotate_right(1);
        }
        Ok(h)
    } else {
        Err(UserError::EmptyError)
    }
}

fn ok_response<H: Serialize, D: Serialize>(headers: H, data: D) -> WebResponse<H, D> {
    WebResponse {
        status: 200,
        err: "".to_string(),
        headers,
        data,
    }
}
