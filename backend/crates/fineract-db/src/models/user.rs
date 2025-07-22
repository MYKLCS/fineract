//! User database model

use chrono::{DateTime, Utc};
use diesel::prelude::*;
// Core types will be used when implementing the full model
use serde::{Deserialize, Serialize};
use uuid::Uuid;

/// User database model
#[derive(Debug, Clone, Queryable, Insertable, Serialize, Deserialize)]
#[diesel(table_name = crate::schema::users)]
pub struct User {
    pub id: i64,
    pub external_id: Uuid,
    pub username: String,
    pub email: String,
    pub password_hash: String,
    pub first_name: String,
    pub last_name: String,
    pub status: String,
    pub created_at: DateTime<Utc>,
    pub updated_at: Option<DateTime<Utc>>,
    pub last_login: Option<DateTime<Utc>>,
    pub is_system_user: bool,
}

/// User creation request
#[derive(Debug, Clone, Insertable)]
#[diesel(table_name = crate::schema::users)]
pub struct NewUser {
    pub external_id: Uuid,
    pub username: String,
    pub email: String,
    pub password_hash: String,
    pub first_name: String,
    pub last_name: String,
    pub status: String,
    pub created_at: DateTime<Utc>,
    pub is_system_user: bool,
}