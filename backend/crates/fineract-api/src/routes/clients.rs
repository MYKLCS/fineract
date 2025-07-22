//! Client API routes

use axum::{
    extract::{Path, State},
    http::StatusCode,
    response::Json,
    routing::{get, post},
    Router,
};
use fineract_client::{ClientService, CreateClientRequest};
use fineract_core::ExternalId;
use serde_json::Value;

use crate::AppState;

/// Create client routes
pub fn create_routes() -> Router<AppState> {
    Router::new()
        .route("/", post(create_client).get(list_clients))
        .route("/:id", get(get_client))
}

/// Create a new client
async fn create_client(
    State(_state): State<AppState>,
    Json(_request): Json<CreateClientRequest>,
) -> Result<Json<Value>, StatusCode> {
    // This will be implemented in Phase 4
    Ok(Json(serde_json::json!({
        "message": "Client creation endpoint - to be implemented in Phase 4"
    })))
}

/// Get a client by ID
async fn get_client(
    State(_state): State<AppState>,
    Path(_id): Path<String>,
) -> Result<Json<Value>, StatusCode> {
    // This will be implemented in Phase 4
    Ok(Json(serde_json::json!({
        "message": "Client retrieval endpoint - to be implemented in Phase 4"
    })))
}

/// List clients
async fn list_clients(State(_state): State<AppState>) -> Result<Json<Value>, StatusCode> {
    // This will be implemented in Phase 4
    Ok(Json(serde_json::json!({
        "message": "Client listing endpoint - to be implemented in Phase 4"
    })))
}