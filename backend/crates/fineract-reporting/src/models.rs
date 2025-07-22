//! Reporting domain models

use serde::{Deserialize, Serialize};

/// Report placeholder
#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Report {
    pub name: String,
    pub description: String,
}