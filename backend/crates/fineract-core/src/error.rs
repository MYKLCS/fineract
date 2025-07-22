//! Core error types for Fineract

use thiserror::Error;

/// Main error type for Fineract operations
#[derive(Error, Debug)]
pub enum Error {
    #[error("Database error: {0}")]
    Database(#[from] diesel::result::Error),
    
    #[error("Validation error: {0}")]
    Validation(String),
    
    #[error("Authentication error: {0}")]
    Authentication(String),
    
    #[error("Authorization error: {0}")]
    Authorization(String),
    
    #[error("Business logic error: {0}")]
    BusinessLogic(String),
    
    #[error("Configuration error: {0}")]
    Configuration(String),
    
    #[error("External service error: {0}")]
    ExternalService(String),
    
    #[error("Serialization error: {0}")]
    Serialization(#[from] serde_json::Error),
    
    #[error("UUID parsing error: {0}")]
    UuidParsing(#[from] uuid::Error),
    
    #[error("Date/time parsing error: {0}")]
    DateTime(#[from] chrono::ParseError),
    
    #[error("Internal error: {0}")]
    Internal(String),
    
    #[error("Not found: {resource} with ID {id}")]
    NotFound { resource: String, id: String },
    
    #[error("Conflict: {0}")]
    Conflict(String),
    
    #[error("Rate limit exceeded")]
    RateLimitExceeded,
    
    #[error("Service unavailable: {0}")]
    ServiceUnavailable(String),
}

/// Result type alias for Fineract operations
pub type Result<T> = std::result::Result<T, Error>;

impl Error {
    /// Create a validation error
    pub fn validation<S: Into<String>>(msg: S) -> Self {
        Self::Validation(msg.into())
    }
    
    /// Create an authentication error
    pub fn authentication<S: Into<String>>(msg: S) -> Self {
        Self::Authentication(msg.into())
    }
    
    /// Create an authorization error
    pub fn authorization<S: Into<String>>(msg: S) -> Self {
        Self::Authorization(msg.into())
    }
    
    /// Create a business logic error
    pub fn business_logic<S: Into<String>>(msg: S) -> Self {
        Self::BusinessLogic(msg.into())
    }
    
    /// Create a not found error
    pub fn not_found<S: Into<String>>(resource: S, id: S) -> Self {
        Self::NotFound {
            resource: resource.into(),
            id: id.into(),
        }
    }
    
    /// Create a conflict error
    pub fn conflict<S: Into<String>>(msg: S) -> Self {
        Self::Conflict(msg.into())
    }
    
    /// Create an internal error
    pub fn internal<S: Into<String>>(msg: S) -> Self {
        Self::Internal(msg.into())
    }
}