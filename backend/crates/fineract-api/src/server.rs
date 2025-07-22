//! HTTP server implementation

use crate::{routes, AppConfig};
use axum::{
    http::StatusCode,
    response::Json,
    routing::get,
    Router,
};
use fineract_db::DatabaseManager;
use serde_json::{json, Value};
use std::net::SocketAddr;
use tower_http::{
    cors::{Any, CorsLayer},
    trace::TraceLayer,
};
use tracing::info;

/// Application state
#[derive(Clone)]
pub struct AppState {
    pub db: DatabaseManager,
    pub config: AppConfig,
}

/// Start the HTTP server
pub async fn start_server(config: AppConfig) -> Result<(), Box<dyn std::error::Error>> {
    // Initialize database
    let db = DatabaseManager::new(&config.database_url).await?;
    
    // Create application state
    let state = AppState { db, config: config.clone() };
    
    // Build the router
    let app = create_router(state);
    
    // Create socket address
    let addr = SocketAddr::new(config.host.parse()?, config.port);
    
    info!("Server listening on {}", addr);
    
    // Start the server
    let listener = tokio::net::TcpListener::bind(addr).await?;
    axum::serve(listener, app).await?;
    
    Ok(())
}

/// Create the application router
fn create_router(state: AppState) -> Router {
    Router::new()
        .route("/", get(root))
        .route("/health", get(health_check))
        .nest("/api/v1", routes::create_api_routes())
        .layer(
            CorsLayer::new()
                .allow_origin(Any) // TODO: Configure properly in production
                .allow_methods(Any)
                .allow_headers(Any),
        )
        .layer(TraceLayer::new_for_http())
        .with_state(state)
}

/// Root handler
async fn root() -> Json<Value> {
    Json(json!({
        "name": "Fineract API",
        "version": "0.1.0",
        "description": "Modern Rust implementation of Apache Fineract microfinance platform"
    }))
}

/// Health check handler
async fn health_check() -> Result<Json<Value>, StatusCode> {
    Ok(Json(json!({
        "status": "healthy",
        "timestamp": chrono::Utc::now().to_rfc3339()
    })))
}