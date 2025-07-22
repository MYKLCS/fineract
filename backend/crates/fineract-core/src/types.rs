//! Core value types and domain primitives

use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use std::fmt;
use uuid::Uuid;
use validator::Validate;

/// External identifier for entities (exposed to clients)
#[derive(Debug, Clone, PartialEq, Eq, Hash, Serialize, Deserialize)]
#[serde(transparent)]
pub struct ExternalId(pub Uuid);

impl ExternalId {
    /// Generate a new random external ID
    pub fn new() -> Self {
        Self(Uuid::new_v4())
    }
    
    /// Parse from string
    pub fn parse(s: &str) -> Result<Self, uuid::Error> {
        Ok(Self(Uuid::parse_str(s)?))
    }
}

impl Default for ExternalId {
    fn default() -> Self {
        Self::new()
    }
}

impl fmt::Display for ExternalId {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "{}", self.0)
    }
}

impl From<Uuid> for ExternalId {
    fn from(uuid: Uuid) -> Self {
        Self(uuid)
    }
}

impl From<ExternalId> for Uuid {
    fn from(id: ExternalId) -> Self {
        id.0
    }
}

/// Internal identifier for database records
#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash, Serialize, Deserialize)]
#[serde(transparent)]
pub struct InternalId(pub i64);

impl InternalId {
    pub fn new(id: i64) -> Self {
        Self(id)
    }
}

impl From<i64> for InternalId {
    fn from(id: i64) -> Self {
        Self(id)
    }
}

impl From<InternalId> for i64 {
    fn from(id: InternalId) -> Self {
        id.0
    }
}

/// Money amount with currency
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize, Validate)]
pub struct Money {
    /// Amount in minor units (e.g., cents for USD)
    pub amount: i64,
    /// ISO 4217 currency code
    #[validate(length(equal = 3))]
    pub currency: String,
}

impl Money {
    /// Create a new Money instance
    pub fn new(amount: i64, currency: impl Into<String>) -> Self {
        Self {
            amount,
            currency: currency.into(),
        }
    }
    
    /// Create zero amount
    pub fn zero(currency: impl Into<String>) -> Self {
        Self::new(0, currency)
    }
    
    /// Check if amount is zero
    pub fn is_zero(&self) -> bool {
        self.amount == 0
    }
    
    /// Check if amount is positive
    pub fn is_positive(&self) -> bool {
        self.amount > 0
    }
    
    /// Check if amount is negative
    pub fn is_negative(&self) -> bool {
        self.amount < 0
    }
    
    /// Add two money amounts (must be same currency)
    pub fn add(&self, other: &Money) -> Result<Money, crate::Error> {
        if self.currency != other.currency {
            return Err(crate::Error::validation(
                format!("Cannot add different currencies: {} and {}", 
                       self.currency, other.currency)
            ));
        }
        Ok(Money::new(self.amount + other.amount, &self.currency))
    }
    
    /// Subtract two money amounts (must be same currency)
    pub fn subtract(&self, other: &Money) -> Result<Money, crate::Error> {
        if self.currency != other.currency {
            return Err(crate::Error::validation(
                format!("Cannot subtract different currencies: {} and {}", 
                       self.currency, other.currency)
            ));
        }
        Ok(Money::new(self.amount - other.amount, &self.currency))
    }
}

/// Address information
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize, Validate)]
pub struct Address {
    #[validate(length(min = 1, max = 255))]
    pub street: String,
    #[validate(length(min = 1, max = 100))]
    pub city: String,
    #[validate(length(max = 20))]
    pub postal_code: Option<String>,
    #[validate(length(max = 100))]
    pub state_province: Option<String>,
    #[validate(length(equal = 2))]
    pub country_code: String, // ISO 3166-1 alpha-2
}

/// Contact information
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize, Validate)]
pub struct ContactInfo {
    #[validate(email)]
    pub email: Option<String>,
    pub phone: Option<String>,
    pub mobile: Option<String>,
}

/// Audit information for entities
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct AuditInfo {
    pub created_at: DateTime<Utc>,
    pub created_by: ExternalId,
    pub updated_at: Option<DateTime<Utc>>,
    pub updated_by: Option<ExternalId>,
    pub version: i32, // For optimistic locking
}

impl AuditInfo {
    /// Create new audit info
    pub fn new(created_by: ExternalId) -> Self {
        Self {
            created_at: Utc::now(),
            created_by,
            updated_at: None,
            updated_by: None,
            version: 1,
        }
    }
    
    /// Update audit info
    pub fn update(&mut self, updated_by: ExternalId) {
        self.updated_at = Some(Utc::now());
        self.updated_by = Some(updated_by);
        self.version += 1;
    }
}

/// Common status enumeration
#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum Status {
    Active,
    Inactive,
    Pending,
    Suspended,
    Closed,
    Cancelled,
}

impl fmt::Display for Status {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Status::Active => write!(f, "ACTIVE"),
            Status::Inactive => write!(f, "INACTIVE"),
            Status::Pending => write!(f, "PENDING"),
            Status::Suspended => write!(f, "SUSPENDED"),
            Status::Closed => write!(f, "CLOSED"),
            Status::Cancelled => write!(f, "CANCELLED"),
        }
    }
}

/// Pagination parameters
#[derive(Debug, Clone, Serialize, Deserialize, Validate)]
pub struct Pagination {
    #[validate(range(min = 0))]
    pub offset: u64,
    #[validate(range(min = 1, max = 1000))]
    pub limit: u64,
}

impl Default for Pagination {
    fn default() -> Self {
        Self {
            offset: 0,
            limit: 50,
        }
    }
}

/// Paginated response
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct PaginatedResponse<T> {
    pub data: Vec<T>,
    pub total: u64,
    pub offset: u64,
    pub limit: u64,
}

impl<T> PaginatedResponse<T> {
    pub fn new(data: Vec<T>, total: u64, offset: u64, limit: u64) -> Self {
        Self {
            data,
            total,
            offset,
            limit,
        }
    }
}