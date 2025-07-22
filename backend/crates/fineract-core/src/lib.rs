//! # Fineract Core
//!
//! Core types, traits, and utilities shared across all Fineract modules.
//! This crate provides the foundational building blocks for the entire system.

pub mod error;
pub mod types;
pub mod traits;
pub mod utils;

// Re-export commonly used types
pub use error::{Error, Result};
pub use types::*;
pub use traits::*;