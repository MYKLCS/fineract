//! Client domain models

use fineract_core::{Address, AuditInfo, ContactInfo, ExternalId, Status, Validatable};
use serde::{Deserialize, Serialize};
use validator::Validate;

/// Client entity
#[derive(Debug, Clone, Serialize, Deserialize, Validate)]
pub struct Client {
    pub external_id: ExternalId,
    #[validate(length(min = 1, max = 100))]
    pub first_name: String,
    #[validate(length(min = 1, max = 100))]
    pub last_name: String,
    #[validate(length(max = 100))]
    pub middle_name: Option<String>,
    pub display_name: String,
    pub mobile_number: Option<String>,
    pub date_of_birth: Option<chrono::NaiveDate>,
    pub gender: Option<Gender>,
    pub client_type: ClientType,
    pub status: Status,
    pub address: Option<Address>,
    pub contact_info: Option<ContactInfo>,
    pub audit_info: AuditInfo,
}

/// Client type enumeration
#[derive(Debug, Clone, Copy, PartialEq, Eq, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum ClientType {
    Individual,
    Entity,
}

/// Gender enumeration
#[derive(Debug, Clone, Copy, PartialEq, Eq, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum Gender {
    Male,
    Female,
    Other,
}

/// Client creation request
#[derive(Debug, Clone, Serialize, Deserialize, Validate)]
pub struct CreateClientRequest {
    #[validate(length(min = 1, max = 100))]
    pub first_name: String,
    #[validate(length(min = 1, max = 100))]
    pub last_name: String,
    #[validate(length(max = 100))]
    pub middle_name: Option<String>,
    pub mobile_number: Option<String>,
    pub date_of_birth: Option<chrono::NaiveDate>,
    pub gender: Option<Gender>,
    pub client_type: ClientType,
    pub address: Option<Address>,
    pub contact_info: Option<ContactInfo>,
}

/// Client update request
#[derive(Debug, Clone, Serialize, Deserialize, Validate)]
pub struct UpdateClientRequest {
    #[validate(length(min = 1, max = 100))]
    pub first_name: Option<String>,
    #[validate(length(min = 1, max = 100))]
    pub last_name: Option<String>,
    #[validate(length(max = 100))]
    pub middle_name: Option<String>,
    pub mobile_number: Option<String>,
    pub date_of_birth: Option<chrono::NaiveDate>,
    pub gender: Option<Gender>,
    pub address: Option<Address>,
    pub contact_info: Option<ContactInfo>,
}

impl Validatable for Client {
    fn validate(&self) -> fineract_core::Result<()> {
        Validate::validate(self)
            .map_err(|e| fineract_core::Error::validation(format!("Client validation failed: {}", e)))
    }
}