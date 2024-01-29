use actix_web::{post, web, Responder};
use anyhow::Result;

use mongodb::{bson::doc};
use serde::{Deserialize, Serialize};
use serde_json::Value;

use crate::{
    error::{self},
    AppState,
};

use super::ok_response;

#[derive(Debug, Serialize, Deserialize)]
struct Title {
    pub title: String,
    pub describe: String,
}

#[post("/api/title")]
async fn update_title(
    db: web::Data<AppState>,
    title: web::Json<Title>,
) -> Result<impl Responder, error::UserError> {
    let title = title.0;
    let collection = &db.db.collection::<Title>("title");
    collection
        .find_one_and_update(
            doc! {},
            doc! { "$set": { "title": title.title, "describe": title.describe }},
            None,
        )
        .await
        .unwrap();
    Ok(web::Json(ok_response(Value::Null, Value::Null)))
}
