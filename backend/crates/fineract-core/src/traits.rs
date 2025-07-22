//! Core traits for domain entities and services

use crate::{AuditInfo, ExternalId, InternalId, Result};
use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};

/// Trait for entities that can be audited
pub trait Auditable {
    /// Get audit information
    fn audit_info(&self) -> &AuditInfo;
    
    /// Get mutable audit information
    fn audit_info_mut(&mut self) -> &mut AuditInfo;
    
    /// Update audit information
    fn update_audit(&mut self, updated_by: ExternalId) {
        self.audit_info_mut().update(updated_by);
    }
}

/// Trait for entities with external IDs
pub trait HasExternalId {
    /// Get the external ID
    fn external_id(&self) -> &ExternalId;
}

/// Trait for entities with internal IDs
pub trait HasInternalId {
    /// Get the internal ID
    fn internal_id(&self) -> Option<InternalId>;
}

/// Trait for entities that can be validated
pub trait Validatable {
    /// Validate the entity
    fn validate(&self) -> Result<()>;
}

/// Trait for repository operations
#[async_trait::async_trait]
pub trait Repository<T> {
    /// Find entity by external ID
    async fn find_by_external_id(&self, id: &ExternalId) -> Result<Option<T>>;
    
    /// Find entity by internal ID
    async fn find_by_internal_id(&self, id: InternalId) -> Result<Option<T>>;
    
    /// Save entity
    async fn save(&self, entity: &T) -> Result<T>;
    
    /// Delete entity by external ID
    async fn delete(&self, id: &ExternalId) -> Result<()>;
    
    /// Check if entity exists
    async fn exists(&self, id: &ExternalId) -> Result<bool>;
}

/// Trait for service operations
#[async_trait::async_trait]
pub trait Service<T> {
    type CreateRequest;
    type UpdateRequest;
    type Query;
    
    /// Create a new entity
    async fn create(&self, request: Self::CreateRequest) -> Result<T>;
    
    /// Update an existing entity
    async fn update(&self, id: &ExternalId, request: Self::UpdateRequest) -> Result<T>;
    
    /// Get entity by ID
    async fn get(&self, id: &ExternalId) -> Result<T>;
    
    /// Delete entity
    async fn delete(&self, id: &ExternalId) -> Result<()>;
    
    /// List entities with query
    async fn list(&self, query: Self::Query) -> Result<Vec<T>>;
}

/// Trait for entities that have a lifecycle state
pub trait Stateful {
    type State;
    
    /// Get current state
    fn state(&self) -> Self::State;
    
    /// Transition to new state
    fn transition_to(&mut self, new_state: Self::State) -> Result<()>;
    
    /// Check if transition is valid
    fn can_transition_to(&self, new_state: Self::State) -> bool;
}

/// Trait for entities that can be soft deleted
pub trait SoftDeletable {
    /// Check if entity is deleted
    fn is_deleted(&self) -> bool;
    
    /// Mark entity as deleted
    fn mark_deleted(&mut self, deleted_by: ExternalId);
    
    /// Get deletion timestamp
    fn deleted_at(&self) -> Option<DateTime<Utc>>;
    
    /// Get who deleted the entity
    fn deleted_by(&self) -> Option<&ExternalId>;
}

/// Trait for entities that can be versioned
pub trait Versionable {
    /// Get current version
    fn version(&self) -> i32;
    
    /// Increment version
    fn increment_version(&mut self);
}

/// Trait for domain events
pub trait DomainEvent: Send + Sync + Clone + Serialize + for<'de> Deserialize<'de> {
    /// Event type identifier
    fn event_type(&self) -> &'static str;
    
    /// Event timestamp
    fn timestamp(&self) -> DateTime<Utc>;
    
    /// Entity ID that triggered the event
    fn entity_id(&self) -> &ExternalId;
    
    /// Event payload
    fn payload(&self) -> serde_json::Value;
}

/// Trait for entities that can publish domain events
pub trait EventPublisher {
    type Event: DomainEvent;
    
    /// Publish a domain event
    async fn publish(&self, event: Self::Event) -> Result<()>;
}

/// Trait for command handlers
#[async_trait::async_trait]
pub trait CommandHandler<C, R> {
    /// Handle a command
    async fn handle(&self, command: C) -> Result<R>;
}

/// Trait for query handlers
#[async_trait::async_trait]
pub trait QueryHandler<Q, R> {
    /// Handle a query
    async fn handle(&self, query: Q) -> Result<R>;
}