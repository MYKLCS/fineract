//! # Fineract Client Domain
//!
//! Client management domain logic for Fineract.

pub mod models;
pub mod services;
pub mod repositories;

// Re-export commonly used types
pub use models::*;
pub use services::*;