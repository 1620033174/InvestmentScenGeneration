use actix_web::{
    error,
    http::{header::ContentType, StatusCode},
    HttpResponse,
};
use derive_more::{Display, Error};
use std::fmt::{Display, Formatter};

use crate::WebResponse;

#[derive(Debug, Display, Error)]
pub enum UserError {
    #[display(fmt = "字段验证失败: {}", field)]
    ValidationError { field: String },

    #[display(fmt = "服务器内部错误，请稍后重试")]
    InternalError,

    #[display(fmt = "未搜索到数据")]
    EmptyError,
}

impl error::ResponseError for UserError {
    fn error_response(&self) -> HttpResponse {
        let status = match *self {
            UserError::InternalError => 500,
            UserError::ValidationError { .. } => 400,
            UserError::EmptyError => 10001,
        };
        HttpResponse::build(self.status_code())
            .insert_header(ContentType::json())
            .json(WebResponse {
                status,
                err: self.to_string(),
                headers: serde_json::Value::Null,
                data: serde_json::Value::Null,
            })
    }

    fn status_code(&self) -> StatusCode {
        match *self {
            UserError::InternalError => StatusCode::INTERNAL_SERVER_ERROR,
            UserError::ValidationError { .. } => StatusCode::BAD_REQUEST,
            UserError::EmptyError => StatusCode::OK
        }
    }
}

/// Necessary because of this issue: https://github.com/actix/actix-web/issues/1711
#[derive(Debug)]
pub struct Error(anyhow::Error);

impl Display for Error {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        std::fmt::Display::fmt(&self.0, f)
    }
}

impl<T> From<T> for Error
where
    T: Into<anyhow::Error>,
{
    fn from(t: T) -> Self {
        Error(t.into())
    }
}

impl error::ResponseError for Error {}
