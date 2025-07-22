use config::{Config, ConfigError, Environment, File};
use serde::Deserialize;
use std::env;

#[derive(Debug, Deserialize, Clone)]
pub struct AppConfig {
    pub port: u16,
    pub database_url: String,
    pub jwt_secret: String,
    pub cors_origins: Vec<String>,
    pub log_level: String,
    pub environment: String,
}

impl AppConfig {
    pub fn load() -> Result<Self, ConfigError> {
        let run_mode = env::var("RUN_MODE").unwrap_or_else(|_| "development".into());

        let config = Config::builder()
            // Start with default settings
            .add_source(File::with_name("config/default"))
            // Add environment specific settings
            .add_source(File::with_name(&format!("config/{}", run_mode)).required(false))
            // Add local settings
            .add_source(File::with_name("config/local").required(false))
            // Add environment variables with prefix "FINERACT_"
            .add_source(Environment::with_prefix("FINERACT").separator("__"))
            .build()?;

        config.try_deserialize()
    }
}

impl Default for AppConfig {
    fn default() -> Self {
        Self {
            port: 8443,
            database_url: "postgres://fineract:fineract@localhost:5432/fineract".to_string(),
            jwt_secret: "default-secret-change-in-production".to_string(),
            cors_origins: vec!["http://localhost:3000".to_string()],
            log_level: "info".to_string(),
            environment: "development".to_string(),
        }
    }
}