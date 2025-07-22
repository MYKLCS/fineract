//! # Fineract Savings Domain
//!
//! Savings management domain logic for Fineract.

pub mod models;
pub mod services;

// Re-export commonly used types
pub use models::*;
pub use services::*;