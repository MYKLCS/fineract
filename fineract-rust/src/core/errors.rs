use axum::{
    http::StatusCode,
    response::{IntoResponse, Response},
    Json,
};
use serde_json::json;
use thiserror::Error;

#[derive(Error, Debug)]
pub enum FineractError {
    #[error("Database error: {0}")]
    Database(#[from] diesel::result::Error),

    #[error("Connection pool error: {0}")]
    Pool(#[from] r2d2::Error),

    #[error("Validation error: {0}")]
    Validation(String),

    #[error("Authentication error: {0}")]
    Authentication(String),

    #[error("Authorization error: {0}")]
    Authorization(String),

    #[error("Not found: {0}")]
    NotFound(String),

    #[error("Conflict: {0}")]
    Conflict(String),

    #[error("Internal server error: {0}")]
    Internal(String),

    #[error("Configuration error: {0}")]
    Configuration(String),

    #[error("Serialization error: {0}")]
    Serialization(#[from] serde_json::Error),

    #[error("External service error: {0}")]
    ExternalService(String),
}

impl IntoResponse for FineractError {
    fn into_response(self) -> Response {
        let (status, error_message) = match self {
            FineractError::Database(_) => {
                (StatusCode::INTERNAL_SERVER_ERROR, "Database error occurred")
            }
            FineractError::Pool(_) => {
                (StatusCode::INTERNAL_SERVER_ERROR, "Database connection error")
            }
            FineractError::Validation(msg) => (StatusCode::BAD_REQUEST, msg),
            FineractError::Authentication(msg) => (StatusCode::UNAUTHORIZED, msg),
            FineractError::Authorization(msg) => (StatusCode::FORBIDDEN, msg),
            FineractError::NotFound(msg) => (StatusCode::NOT_FOUND, msg),
            FineractError::Conflict(msg) => (StatusCode::CONFLICT, msg),
            FineractError::Internal(msg) => (StatusCode::INTERNAL_SERVER_ERROR, msg),
            FineractError::Configuration(msg) => (StatusCode::INTERNAL_SERVER_ERROR, msg),
            FineractError::Serialization(_) => {
                (StatusCode::BAD_REQUEST, "Invalid request format")
            }
            FineractError::ExternalService(msg) => (StatusCode::BAD_GATEWAY, msg),
        };

        let body = Json(json!({
            "error": {
                "code": status.as_u16(),
                "message": error_message,
                "timestamp": chrono::Utc::now().to_rfc3339()
            }
        }));

        (status, body).into_response()
    }
}

pub type Result<T> = std::result::Result<T, FineractError>;