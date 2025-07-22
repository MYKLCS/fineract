use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use uuid::Uuid;

// Common ID types
pub type ClientId = Uuid;
pub type LoanId = Uuid;
pub type SavingsId = Uuid;
pub type AccountId = Uuid;
pub type UserId = Uuid;
pub type TenantId = Uuid;

// Money and currency types
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Money {
    pub amount: f64,
    pub currency_code: String,
}

impl Money {
    pub fn new(amount: f64, currency_code: &str) -> Self {
        Self {
            amount,
            currency_code: currency_code.to_string(),
        }
    }
}

// Date range for queries
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct DateRange {
    pub from_date: DateTime<Utc>,
    pub to_date: DateTime<Utc>,
}

// Pagination
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct PaginationParams {
    pub page: Option<i64>,
    pub size: Option<i64>,
    pub sort_by: Option<String>,
    pub sort_order: Option<SortOrder>,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub enum SortOrder {
    #[serde(rename = "asc")]
    Ascending,
    #[serde(rename = "desc")]
    Descending,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct PaginatedResponse<T> {
    pub data: Vec<T>,
    pub total_count: i64,
    pub page: i64,
    pub size: i64,
    pub total_pages: i64,
}

// Status enums
#[derive(Debug, Clone, Serialize, Deserialize)]
pub enum AccountStatus {
    #[serde(rename = "ACTIVE")]
    Active,
    #[serde(rename = "INACTIVE")]
    Inactive,
    #[serde(rename = "CLOSED")]
    Closed,
    #[serde(rename = "PENDING")]
    Pending,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub enum LoanStatus {
    #[serde(rename = "ACTIVE")]
    Active,
    #[serde(rename = "CLOSED")]
    Closed,
    #[serde(rename = "PENDING")]
    Pending,
    #[serde(rename = "REJECTED")]
    Rejected,
    #[serde(rename = "APPROVED")]
    Approved,
}

// Common metadata
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Metadata {
    pub created_at: DateTime<Utc>,
    pub created_by: UserId,
    pub updated_at: Option<DateTime<Utc>>,
    pub updated_by: Option<UserId>,
    pub version: i32,
}