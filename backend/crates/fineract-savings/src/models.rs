//! Savings domain models

use fineract_core::{ExternalId, Money, Status};
use serde::{Deserialize, Serialize};

/// Savings account entity placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct SavingsAccount {
    pub external_id: ExternalId,
    pub client_id: ExternalId,
    pub product_id: ExternalId,
    pub balance: Money,
    pub status: Status,
}

/// Savings account creation request placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct CreateSavingsAccountRequest {
    pub client_id: ExternalId,
    pub product_id: ExternalId,
}