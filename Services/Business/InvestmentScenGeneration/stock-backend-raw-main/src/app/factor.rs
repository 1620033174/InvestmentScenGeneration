use std::{
    collections::{BTreeMap},
};

use actix_web::{get, web, Responder};
use anyhow::anyhow;
use futures::TryStreamExt;
use mongodb::{bson::doc, options::FindOptions};
use serde::{Deserialize, Serialize};
use serde_json::{json, Value};

use crate::{
    app::{generate_headers, ok_response},
    error::{self, UserError},
    AppState,
};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Factor {
    pub stock_id: String,
    pub _stock_name: String,
    pub _time: String,
    pub _quarter: String,
    pub 因子名称: String,
    pub 个股取值: f64,
    pub 行业名称: String,
    pub 行业均值: f64,
    pub 行业中值: f64,
    pub 行业排名: String,
}

#[get("/api/compare/{stock_id}/{time}")]
async fn compare_stock_with_time(
    db: web::Data<AppState>,
    path: web::Path<(String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (stock_id, time) = path.into_inner();
    info!("/api/compare/{stock_id}/{time}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    } else if time.is_empty() {
        return Err(UserError::ValidationError {
            field: "time".to_string(),
        });
    }
    let collection = &db.db.collection::<Factor>("factors");
    let filter = doc! { "stock_id": stock_id, "_time": time };
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(filter, options).await.expect("搜索失败");
    let factors: Vec<Factor> = cursor.try_collect().await.expect("生成数组失败");
    if factors.is_empty() {
        warn!("factor 表为空");
        return Err(UserError::EmptyError)
    }
    let values = generate_compare_by_factor(factors);
    let headers: Vec<String> = generate_headers(values.clone())?;
    Ok(web::Json(ok_response(headers, values)))
}

fn generate_compare_by_factor(factors: Vec<Factor>) -> Vec<Value> {
    // map factor vector to factor table
    let mut table: Vec<BTreeMap<String, Value>> = Vec::with_capacity(4);

    // basic info
    for _i in 0..3 {
        let mut map: BTreeMap<String, Value> = BTreeMap::new();
        map.insert(
            "stock_id".to_string(),
            Value::String(factors[0].stock_id.clone()),
        );
        map.insert("_time".to_string(), Value::String(factors[0]._time.clone()));
        table.push(map);
    }

    // 个股信息
    table[0].insert(
        "meta".to_string(),
        Value::String(factors[0]._stock_name.clone()),
    );
    for factor in &factors {
        let value = json!(factor.个股取值);
        table[0].insert(factor.因子名称.clone(), value);
        info!("{}", factor.因子名称);
        info!("length: {}", factors.len());
    }

    // 行业均值
    table[1].insert("meta".to_string(), Value::String(factors[0].行业名称.clone()));
    for factor in &factors {
        let value = json!(factor.行业均值);
        table[1].insert(factor.因子名称.clone(), value);
    }

    // 行业排名
    table[2].insert("meta".to_string(), Value::String("行业排名".to_string()));
    for factor in &factors {
        let value = json!(factor.行业排名);
        table[2].insert(factor.因子名称.clone(), value);
    }

    let mut ret: Vec<Value> = Vec::with_capacity(4);
    for row in table {
        let value = json!(row);
        ret.push(value);
    }

    ret
}

#[get("/api/factor/{factor_name}/{stock_id}/{time}")]
async fn query_factor_with_time(
    db: web::Data<AppState>,
    path: web::Path<(String, String, String)>,
) -> Result<impl Responder, error::UserError> {
    let (factor_name, stock_id, time) = path.into_inner();
    info!("/api/factor/{factor_name}/{stock_id}/{time}");
    if stock_id.is_empty() {
        return Err(UserError::ValidationError {
            field: "stock_id".to_string(),
        });
    } else if time.is_empty() {
        return Err(UserError::ValidationError {
            field: "time".to_string(),
        });
    }
    let collection = &db.db.collection::<Factor>("factors");
    let filter = doc! { "stock_id": &stock_id, "_time": &time, "因子名称": &factor_name };
    let options = FindOptions::builder()
        .limit(1)
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(filter, options).await.expect("搜索失败");
    let factors: Vec<Factor> = cursor.try_collect().await.expect("搜索失败");

    // 获取季度列表
    let quarters = get_query_quarter(factors[0]._quarter.clone())
        .expect("解析季度数据失败，请确认格式为 2021q2");

    // 根据季度列表获取数据
    info!("查询季度{quarters:?}");
    let filter =
        doc! { "stock_id": &stock_id, "因子名称": &factor_name, "_quarter": { "$in": quarters } };
    let options = FindOptions::builder()
        .projection(doc! {"_id": false})
        .build();
    let cursor = collection.find(filter, options).await.expect("搜索失败");
    let mut factors: Vec<Factor> = cursor.try_collect().await.expect("生成数组失败");

    factors.dedup_by_key(|f| f._quarter.clone());
    let values = generate_table_by_factor(factors, factor_name.clone());
    let headers: Vec<String> = generate_headers(values.clone())?;
    Ok(web::Json(ok_response(headers, values)))
}

// 获取前三个季度及本季度列表
fn get_query_quarter(now: String) -> anyhow::Result<Vec<String>> {
    let mut query: Vec<String> = Vec::new();
    for i in (0..=3_u8).rev() {
        query.push(year_quarter_minus(&now, i)?)
    }
    Ok(query)
}

fn year_quarter_minus(now: &str, quarter: u8) -> anyhow::Result<String> {
    let now = now.to_lowercase();
    let all: Vec<&str> = now.split('q').collect();
    if all.len() < 2 {
        return Err(anyhow!("解析季度数据失败，请确认格式为 2021q2"));
    }
    let mut now_year = all[0].parse::<u16>()?;
    let mut now_quarter = all[1].parse::<u8>()?;
    if now_quarter <= quarter {
        now_year -= 1;
        now_quarter += 4;
    }
    now_quarter -= quarter;
    Ok(format!("{now_year}q{now_quarter}"))
}

fn generate_table_by_factor(factors: Vec<Factor>, factor_name: String) -> Vec<Value> {
    if factors.is_empty() {
        return Vec::new();
    }
    // last 4 records
    let factors = if factors.len() > 4 {
        factors
            .into_iter()
            .rev()
            .take(4)
            .rev()
            .collect::<Vec<Factor>>()
    } else {
        factors
    };

    // map factor vector to factor table
    let mut table: Vec<BTreeMap<String, Value>> = Vec::with_capacity(4);

    // basic info
    for _i in 0..4 {
        let mut map: BTreeMap<String, Value> = BTreeMap::new();
        map.insert(
            "stock_id".to_string(),
            Value::String(factors[0].stock_id.clone()),
        );
        map.insert("_time".to_string(), Value::String(factors[0]._time.clone()));
        // map.insert(factor_name.to_string(), Value::String(factors[0]._stock_name));
        table.push(map);
    }

    // 个股信息
    table[0].insert(
        factor_name.clone(),
        Value::String(factors[0]._stock_name.clone()),
    );
    for factor in &factors {
        let value = json!(factor.个股取值);
        table[0].insert(factor._quarter.clone(), value);
    }

    // 行业均值
    table[1].insert(factor_name.clone(), Value::String("行业均值".to_string()));
    for factor in &factors {
        let value = json!(factor.行业均值);
        table[1].insert(factor._quarter.clone(), value);
    }

    // 行业中值
    table[2].insert(factor_name.clone(), Value::String("行业中值".to_string()));
    for factor in &factors {
        let value = json!(factor.行业中值);
        table[2].insert(factor._quarter.clone(), value);
    }

    // 行业排名
    table[3].insert(factor_name, Value::String("行业排名".to_string()));
    for factor in &factors {
        let value = json!(factor.行业排名);
        table[3].insert(factor._quarter.clone(), value);
    }

    let mut ret: Vec<Value> = Vec::with_capacity(4);

    for row in table {
        let value = json!(row);
        ret.push(value);
    }

    ret
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_year() {
        println!("{:?}", get_query_quarter("2021q1".to_string()));
        println!("{:?}", get_query_quarter("2021q2".to_string()));
        println!("{:?}", get_query_quarter("2021q3".to_string()));
        println!("{:?}", get_query_quarter("2021q4".to_string()));
    }
}
