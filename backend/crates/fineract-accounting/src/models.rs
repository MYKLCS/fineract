//! Accounting domain models

use fineract_core::{ExternalId, Money};
use serde::{Deserialize, Serialize};

/// Journal entry placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct JournalEntry {
    pub external_id: ExternalId,
    pub description: String,
    pub amount: Money,
}