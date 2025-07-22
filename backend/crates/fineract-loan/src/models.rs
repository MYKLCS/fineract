//! Loan domain models

use fineract_core::{ExternalId, Money, Status};
use serde::{Deserialize, Serialize};

/// Loan entity placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Loan {
    pub external_id: ExternalId,
    pub client_id: ExternalId,
    pub product_id: ExternalId,
    pub principal: Money,
    pub status: Status,
}

/// Loan creation request placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct CreateLoanRequest {
    pub client_id: ExternalId,
    pub product_id: ExternalId,
    pub principal: Money,
}