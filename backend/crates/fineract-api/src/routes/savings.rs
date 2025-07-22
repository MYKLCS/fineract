//! Savings API routes

use axum::{
    extract::State,
    http::StatusCode,
    response::Json,
    routing::get,
    Router,
};
use serde_json::Value;

use crate::AppState;

/// Create savings routes
pub fn create_routes() -> Router<AppState> {
    Router::new()
        .route("/", get(list_savings))
}

/// List savings accounts
async fn list_savings(State(_state): State<AppState>) -> Result<Json<Value>, StatusCode> {
    // This will be implemented in Phase 4
    Ok(Json(serde_json::json!({
        "message": "Savings endpoints - to be implemented in Phase 4"
    })))
}