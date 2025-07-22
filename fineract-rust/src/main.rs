use axum::{
    routing::{get, post},
    Router,
};
use std::net::SocketAddr;
use tracing::{info, Level};
use tracing_subscriber::FmtSubscriber;

mod api;
mod config;
mod core;
mod db;
mod domain;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    // Initialize tracing
    let subscriber = FmtSubscriber::builder()
        .with_max_level(Level::INFO)
        .finish();
    tracing::subscriber::set_global_default(subscriber)?;

    info!("Starting Apache Fineract Rust Server...");

    // Load configuration
    let config = config::AppConfig::load()?;
    info!("Configuration loaded successfully");

    // Initialize database connection
    let db_pool = db::establish_connection(&config.database_url).await?;
    info!("Database connection established");

    // Run database migrations
    db::run_migrations(&db_pool).await?;
    info!("Database migrations completed");

    // Build application router
    let app = Router::new()
        .route("/health", get(health_check))
        .route("/api/v1/health", get(health_check))
        .with_state(db_pool);

    // Start server
    let addr = SocketAddr::from(([127, 0, 0, 1], config.port));
    info!("Server listening on {}", addr);

    axum::Server::bind(&addr)
        .serve(app.into_make_service())
        .await?;

    Ok(())
}

async fn health_check() -> &'static str {
    "OK"
}
