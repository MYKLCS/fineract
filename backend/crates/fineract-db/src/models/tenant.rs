//! Tenant database model

use chrono::{DateTime, Utc};
use diesel::prelude::*;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

/// Tenant database model
#[derive(Debug, Clone, Queryable, Insertable, Serialize, Deserialize)]
#[diesel(table_name = crate::schema::tenants)]
pub struct Tenant {
    pub id: i64,
    pub external_id: Uuid,
    pub name: String,
    pub identifier: String,
    pub schema_name: String,
    pub timezone: String,
    pub country_code: String,
    pub created_at: DateTime<Utc>,
    pub updated_at: Option<DateTime<Utc>>,
    pub is_active: bool,
}

/// Tenant creation request
#[derive(Debug, Clone, Insertable)]
#[diesel(table_name = crate::schema::tenants)]
pub struct NewTenant {
    pub external_id: Uuid,
    pub name: String,
    pub identifier: String,
    pub schema_name: String,
    pub timezone: String,
    pub country_code: String,
    pub created_at: DateTime<Utc>,
    pub is_active: bool,
}