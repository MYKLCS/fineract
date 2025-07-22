//! Loan domain services

use crate::{CreateLoanRequest, Loan};
use fineract_core::{ExternalId, Result};

/// Loan service placeholder
#[derive(Debug, Clone)]
pub struct LoanService;

impl LoanService {
    pub fn new() -> Self {
        Self
    }
    
    pub async fn create_loan(&self, _request: CreateLoanRequest) -> Result<Loan> {
        todo!("Loan service to be implemented in Phase 3")
    }
    
    pub async fn get_loan(&self, _id: &ExternalId) -> Result<Loan> {
        todo!("Loan service to be implemented in Phase 3")
    }
}