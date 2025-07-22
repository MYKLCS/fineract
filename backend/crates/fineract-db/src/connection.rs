//! Database connection management

use deadpool_diesel::postgres::{Manager, Pool, Object};
use fineract_core::Result;
use std::sync::Arc;

/// Database connection pool
pub type DbPool = Pool;

/// Database connection manager
#[derive(Clone)]
pub struct DatabaseManager {
    pool: Arc<DbPool>,
}

impl DatabaseManager {
    /// Create a new database manager
    pub async fn new(database_url: &str) -> Result<Self> {
        let manager = Manager::new(database_url, deadpool_diesel::Runtime::Tokio1);
        let pool = Pool::builder(manager)
            .max_size(50)
            .build()
            .map_err(|e| fineract_core::Error::internal(format!("Failed to create connection pool: {}", e)))?;
        
        Ok(Self {
            pool: Arc::new(pool),
        })
    }
    
    /// Get a connection from the pool
    pub async fn get_connection(&self) -> Result<Object> {
        self.pool
            .get()
            .await
            .map_err(|e| fineract_core::Error::internal(format!("Failed to get connection: {}", e)))
    }
    
    /// Get the pool
    pub fn pool(&self) -> &DbPool {
        &self.pool
    }
}