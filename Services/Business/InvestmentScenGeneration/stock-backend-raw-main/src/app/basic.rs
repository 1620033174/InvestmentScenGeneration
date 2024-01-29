use std::{path, vec};

use actix_web::{get, web, Responder};
use futures::TryStreamExt;
use mongodb::{bson::doc, options::FindOptions};
use serde_json::{json, Value};

use crate::{
    app::{generate_headers, ok_response},
    error::{self, UserError},
    python::{pyo3_runtime::Pyo3Runtime, PythonRuntime},
    AppState,
};

#[get("/api/{table_name}")]
async fn query_all(
    db: web::Data<AppState>,
    path: web::Path<String>,
) -> Result<impl Responder, error::UserError> {
    let table_name = path.into_inner();
    info!("/api/{table_name}");
    let collection = &db.db.collection::<Value>(&table_name);
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(None, options).await.expect("搜索失败");
    let v: Vec<Value> = cursor.try_collect().await.expect("生成数组失败");
    let headers: Vec<String> = generate_headers(v.clone())?;
    Ok(web::Json(ok_response(headers, v)))
}

#[get("/api/{table_name}/{stock_id}")]
async fn query_stock(
    db: web::Data<AppState>,
    path: web::Path<(String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (table_name, stock_id) = path.into_inner();
    info!("/api/{table_name}/{stock_id}");
    let collection = &db.db.collection::<Value>(&table_name);
    let filter = doc! { "stock_id": stock_id };
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection
        .find(filter, options)
        .await
        .map_err(|_| UserError::InternalError)?;
    let v: Vec<Value> = cursor.try_collect().await.expect("生成数组失败");
    let headers: Vec<String> = generate_headers(v.clone())?;
    Ok(web::Json(ok_response(headers, v)))
}

#[get("/api/{table_name}/{stock_id}/{time}")]
async fn query_stock_with_time(
    db: web::Data<AppState>,
    path: web::Path<(String, String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (table_name, stock_id, time) = path.into_inner();
    info!("/api/{table_name}/{stock_id}/{time}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    } else if time.is_empty() {
        return Err(UserError::ValidationError {
            field: "time".to_string(),
        });
    }
    let collection = &db.db.collection::<Value>(&table_name);
    let filter = doc! { "stock_id": stock_id, "_time": time };
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(filter, options).await.expect("搜索失败");
    let v: Vec<Value> = cursor.try_collect().await.expect("生成数组失败");
    let headers: Vec<String> = generate_headers(v.clone())?;
    Ok(web::Json(ok_response(headers, v)))
}

#[get("/api/filters")]
async fn get_filters(db: web::Data<AppState>) -> Result<impl Responder, error::UserError> {
    let collection = &db.db.collection::<Value>("stocks");

    let pipeline =
        vec![doc! { "$group": { "_id": null, "filters": { "$addToSet": "$_selected_time" } } }];
    let mut cursor = collection
        .aggregate(pipeline, None)
        .await
        .map_err(|_| error::UserError::EmptyError)?;
    let filters = cursor
        .try_next()
        .await
        .map_err(|_| UserError::InternalError)?;
    if filters.is_none() {
        return Err(UserError::InternalError);
    }
    let filters = filters
        .unwrap()
        .get_array("filters")
        .map_err(|_| UserError::InternalError)?
        .to_vec();
    Ok(web::Json(ok_response("", filters)))
}

#[get("/api/stocks/{quarter}")]
async fn get_stocks_with_quarter(
    db: web::Data<AppState>,
    path: web::Path<String>,
) -> Result<impl Responder, error::UserError> {
    let quarter = path.into_inner();
    let collection = &db.db.collection::<Value>("stocks");
    let filter = doc! { "_selected_time": quarter };
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(filter, options).await.expect("搜索失败");
    let v: Vec<Value> = cursor.try_collect().await.expect("生成数组失败");
    let v = generate_prices(v).map_err(|e| {
        error!("{e:?}");
        UserError::InternalError
    })?;
    let headers: Vec<String> = generate_headers(v.clone())?;
    Ok(web::Json(ok_response(headers, v)))
}

fn generate_prices(mut stocks: Vec<Value>) -> anyhow::Result<Vec<Value>> {
    if !stocks.is_empty() {
        for stock in stocks.iter_mut() {
            let map = stock.as_object_mut().unwrap();
            map.insert(
                "当前股价(元)".to_string(),
                json!(get_price_by_id(
                    map.get("stock_id").unwrap().as_str().unwrap().to_string(),
                )?),
            );
        }
        return Ok(stocks);
    }
    Ok(stocks)
}

fn get_price_by_id(stock_id: String) -> anyhow::Result<f64> {
    let runtime = Pyo3Runtime::global();
    let path = path::Path::new("algorithm/stockPrice/stockPrice.py");
    let result = runtime.call1_as_f64(path, "get_result", stock_id)?;
    Ok(result)
}
