//! Savings domain services

use crate::{CreateSavingsAccountRequest, SavingsAccount};
use fineract_core::{ExternalId, Result};

/// Savings service placeholder
#[derive(Debug, Clone)]
pub struct SavingsService;

impl SavingsService {
    pub fn new() -> Self {
        Self
    }
    
    pub async fn create_account(&self, _request: CreateSavingsAccountRequest) -> Result<SavingsAccount> {
        todo!("Savings service to be implemented in Phase 3")
    }
    
    pub async fn get_account(&self, _id: &ExternalId) -> Result<SavingsAccount> {
        todo!("Savings service to be implemented in Phase 3")
    }
}