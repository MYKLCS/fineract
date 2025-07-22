use chrono::{DateTime, Utc};
use regex::Regex;
use uuid::Uuid;

/// Validates email format
pub fn is_valid_email(email: &str) -> bool {
    let email_regex = Regex::new(r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$").unwrap();
    email_regex.is_match(email)
}

/// Validates phone number format (basic validation)
pub fn is_valid_phone(phone: &str) -> bool {
    let phone_regex = Regex::new(r"^\+?[1-9]\d{1,14}$").unwrap();
    phone_regex.is_match(phone)
}

/// Validates currency code (ISO 4217)
pub fn is_valid_currency_code(currency: &str) -> bool {
    let currency_regex = Regex::new(r"^[A-Z]{3}$").unwrap();
    currency_regex.is_match(currency)
}

/// Formats money amount with proper decimal places
pub fn format_money(amount: f64, currency_code: &str) -> String {
    format!("{:.2} {}", amount, currency_code)
}

/// Generates a unique identifier
pub fn generate_id() -> Uuid {
    Uuid::new_v4()
}

/// Formats date for display
pub fn format_date(date: &DateTime<Utc>) -> String {
    date.format("%Y-%m-%d").to_string()
}

/// Formats datetime for display
pub fn format_datetime(date: &DateTime<Utc>) -> String {
    date.format("%Y-%m-%d %H:%M:%S UTC").to_string()
}

/// Calculates age from birth date
pub fn calculate_age(birth_date: &DateTime<Utc>) -> u32 {
    let now = Utc::now();
    let age = now.year() - birth_date.year();
    let month_diff = now.month() as i32 - birth_date.month() as i32;
    
    if month_diff < 0 || (month_diff == 0 && now.day() < birth_date.day()) {
        (age - 1) as u32
    } else {
        age as u32
    }
}

/// Validates account number format
pub fn is_valid_account_number(account_number: &str) -> bool {
    // Basic validation - account numbers should be alphanumeric and reasonable length
    let account_regex = Regex::new(r"^[A-Z0-9]{8,20}$").unwrap();
    account_regex.is_match(account_number)
}

/// Sanitizes input string
pub fn sanitize_input(input: &str) -> String {
    input.trim().to_string()
}

/// Validates percentage value
pub fn is_valid_percentage(percentage: f64) -> bool {
    percentage >= 0.0 && percentage <= 100.0
}

/// Rounds money amount to 2 decimal places
pub fn round_money(amount: f64) -> f64 {
    (amount * 100.0).round() / 100.0
}