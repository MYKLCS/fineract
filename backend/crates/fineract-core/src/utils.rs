//! Utility functions and helpers

use chrono::{DateTime, Utc};
use std::collections::HashMap;

/// Generate a random string of specified length
pub fn generate_random_string(length: usize) -> String {
    use rand::Rng;
    const CHARSET: &[u8] = b"ABCDEFGHIJKLMNOPQRSTUVWXYZ\
                            abcdefghijklmnopqrstuvwxyz\
                            0123456789";
    let mut rng = rand::thread_rng();
    
    (0..length)
        .map(|_| {
            let idx = rng.gen_range(0..CHARSET.len());
            CHARSET[idx] as char
        })
        .collect()
}

/// Generate a secure random token
pub fn generate_secure_token() -> String {
    use rand::RngCore;
    use base64::{Engine as _, engine::general_purpose};
    let mut bytes = [0u8; 32];
    rand::thread_rng().fill_bytes(&mut bytes);
    general_purpose::URL_SAFE_NO_PAD.encode(&bytes)
}

/// Hash a password using Argon2
pub fn hash_password(password: &str) -> crate::Result<String> {
    use argon2::{
        password_hash::{rand_core::OsRng, PasswordHasher, SaltString},
        Argon2,
    };
    
    let salt = SaltString::generate(&mut OsRng);
    let argon2 = Argon2::default();
    
    argon2
        .hash_password(password.as_bytes(), &salt)
        .map(|hash| hash.to_string())
        .map_err(|e| crate::Error::internal(format!("Password hashing failed: {}", e)))
}

/// Verify a password against its hash
pub fn verify_password(password: &str, hash: &str) -> crate::Result<bool> {
    use argon2::{
        password_hash::{PasswordHash, PasswordVerifier},
        Argon2,
    };
    
    let parsed_hash = PasswordHash::new(hash)
        .map_err(|e| crate::Error::internal(format!("Invalid password hash: {}", e)))?;
    
    Ok(Argon2::default()
        .verify_password(password.as_bytes(), &parsed_hash)
        .is_ok())
}

/// Convert a HashMap to query parameters string
pub fn hashmap_to_query_string(params: &HashMap<String, String>) -> String {
    if params.is_empty() {
        return String::new();
    }
    
    let query_pairs: Vec<String> = params
        .iter()
        .map(|(key, value)| format!("{}={}", urlencoding::encode(key), urlencoding::encode(value)))
        .collect();
    
    format!("?{}", query_pairs.join("&"))
}

/// Format money amount for display
pub fn format_money(amount: i64, currency: &str, decimal_places: u8) -> String {
    let divisor = 10_i64.pow(decimal_places as u32);
    let whole = amount / divisor;
    let fraction = (amount % divisor).abs();
    
    if decimal_places == 0 {
        format!("{} {}", whole, currency)
    } else {
        format!("{}.{:0width$} {}", whole, fraction, currency, width = decimal_places as usize)
    }
}

/// Parse money amount from string
pub fn parse_money(input: &str, decimal_places: u8) -> crate::Result<i64> {
    let cleaned = input.trim().replace(',', "");
    
    if let Some(dot_pos) = cleaned.find('.') {
        let whole_part = &cleaned[..dot_pos];
        let fraction_part = &cleaned[dot_pos + 1..];
        
        if fraction_part.len() > decimal_places as usize {
            return Err(crate::Error::validation(
                format!("Too many decimal places. Maximum: {}", decimal_places)
            ));
        }
        
        let whole: i64 = whole_part.parse()
            .map_err(|_| crate::Error::validation("Invalid whole number part"))?;
        
        let mut fraction: i64 = fraction_part.parse()
            .map_err(|_| crate::Error::validation("Invalid fraction part"))?;
        
        // Pad fraction with zeros if needed
        let missing_digits = decimal_places as usize - fraction_part.len();
        for _ in 0..missing_digits {
            fraction *= 10;
        }
        
        let multiplier = 10_i64.pow(decimal_places as u32);
        Ok(whole * multiplier + fraction)
    } else {
        let whole: i64 = cleaned.parse()
            .map_err(|_| crate::Error::validation("Invalid number"))?;
        let multiplier = 10_i64.pow(decimal_places as u32);
        Ok(whole * multiplier)
    }
}

/// Calculate compound interest
pub fn calculate_compound_interest(
    principal: f64,
    annual_rate: f64,
    compounds_per_year: u32,
    years: f64,
) -> f64 {
    let rate_per_period = annual_rate / (compounds_per_year as f64);
    let total_periods = (compounds_per_year as f64) * years;
    principal * (1.0 + rate_per_period).powf(total_periods)
}

/// Calculate simple interest
pub fn calculate_simple_interest(principal: f64, annual_rate: f64, years: f64) -> f64 {
    principal * annual_rate * years
}

/// Validate email address format
pub fn is_valid_email(email: &str) -> bool {
    use regex::Regex;
    let email_regex = Regex::new(
        r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
    ).unwrap();
    email_regex.is_match(email)
}

/// Validate phone number format (basic validation)
pub fn is_valid_phone(phone: &str) -> bool {
    use regex::Regex;
    let phone_regex = Regex::new(r"^\+?[1-9]\d{1,14}$").unwrap();
    phone_regex.is_match(&phone.replace(&[' ', '-', '(', ')'][..], ""))
}

/// Get current timestamp in UTC
pub fn now_utc() -> DateTime<Utc> {
    Utc::now()
}

/// Convert timestamp to ISO 8601 string
pub fn timestamp_to_iso8601(timestamp: DateTime<Utc>) -> String {
    timestamp.to_rfc3339()
}

/// Parse ISO 8601 string to timestamp
pub fn iso8601_to_timestamp(iso_string: &str) -> crate::Result<DateTime<Utc>> {
    DateTime::parse_from_rfc3339(iso_string)
        .map(|dt| dt.with_timezone(&Utc))
        .map_err(|e| crate::Error::DateTime(e.into()))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_format_money() {
        assert_eq!(format_money(12345, "USD", 2), "123.45 USD");
        assert_eq!(format_money(1000, "USD", 2), "10.00 USD");
        assert_eq!(format_money(5, "USD", 2), "0.05 USD");
    }

    #[test]
    fn test_parse_money() {
        assert_eq!(parse_money("123.45", 2).unwrap(), 12345);
        assert_eq!(parse_money("10.00", 2).unwrap(), 1000);
        assert_eq!(parse_money("0.05", 2).unwrap(), 5);
        assert_eq!(parse_money("100", 2).unwrap(), 10000);
    }

    #[test]
    fn test_is_valid_email() {
        assert!(is_valid_email("test@example.com"));
        assert!(is_valid_email("user.name+tag@domain.co.uk"));
        assert!(!is_valid_email("invalid.email"));
        assert!(!is_valid_email("@example.com"));
    }

    #[test]
    fn test_is_valid_phone() {
        assert!(is_valid_phone("+1234567890"));
        assert!(is_valid_phone("1234567890"));
        assert!(is_valid_phone("+44 20 7946 0958"));
        assert!(!is_valid_phone("abc123"));
        assert!(!is_valid_phone(""));
    }
}