//! Fineract API Server
//!
//! Main entry point for the Fineract HTTP API server.

use fineract_api::{start_server, AppConfig};
use tracing::info;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    // Initialize tracing
    tracing_subscriber::fmt()
        .with_env_filter(
            tracing_subscriber::EnvFilter::try_from_default_env()
                .unwrap_or_else(|_| "fineract_api=info,tower_http=debug".into()),
        )
        .init();

    // Load configuration
    let config = AppConfig::from_env()?;
    
    info!("Starting Fineract API server on {}:{}", config.host, config.port);
    
    // Start the server
    start_server(config).await?;
    
    Ok(())
}