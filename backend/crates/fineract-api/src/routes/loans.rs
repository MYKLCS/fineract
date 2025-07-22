//! Loan API routes

use axum::{
    extract::State,
    http::StatusCode,
    response::Json,
    routing::get,
    Router,
};
use serde_json::Value;

use crate::AppState;

/// Create loan routes
pub fn create_routes() -> Router<AppState> {
    Router::new()
        .route("/", get(list_loans))
}

/// List loans
async fn list_loans(State(_state): State<AppState>) -> Result<Json<Value>, StatusCode> {
    // This will be implemented in Phase 4
    Ok(Json(serde_json::json!({
        "message": "Loan endpoints - to be implemented in Phase 4"
    })))
}