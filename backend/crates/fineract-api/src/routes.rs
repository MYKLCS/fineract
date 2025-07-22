//! API routes

pub mod clients;
pub mod loans;
pub mod savings;

use axum::{routing::get, Router};

use crate::AppState;

/// Create all API routes
pub fn create_api_routes() -> Router<AppState> {
    Router::new()
        .route("/", get(api_info))
        .nest("/clients", clients::create_routes())
        .nest("/loans", loans::create_routes())
        .nest("/savings", savings::create_routes())
}

/// API info handler
async fn api_info() -> axum::Json<serde_json::Value> {
    axum::Json(serde_json::json!({
        "api": "Fineract API v1",
        "endpoints": [
            "/clients",
            "/loans", 
            "/savings"
        ]
    }))
}