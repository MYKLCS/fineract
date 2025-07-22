//! HTTP middleware

// This module will contain authentication, authorization, and other middleware
// To be implemented in Phase 1

pub mod auth;
pub mod validation;

pub use auth::*;
pub use validation::*;