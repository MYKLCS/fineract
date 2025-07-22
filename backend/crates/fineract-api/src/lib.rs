//! # Fineract API
//!
//! HTTP API server for Fineract microfinance platform.

pub mod server;
pub mod routes;
pub mod middleware;
pub mod config;

// Re-export commonly used types
pub use config::*;
pub use server::*;