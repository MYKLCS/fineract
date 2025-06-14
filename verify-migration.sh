#!/bin/bash

echo "===== PostgreSQL Migration Verification ====="
echo "Testing that all hardcoded database connections have been removed"
echo

# Set test environment variables
source /Users/michaelwolf/fineract/test-env.sh

echo "1. Checking application.properties for hardcoded values..."
if grep -n "localhost\|jdbc:.*://.*:" /Users/michaelwolf/fineract/fineract-provider/src/main/resources/application.properties; then
    echo "❌ Found hardcoded database connections in application.properties"
else
    echo "✅ No hardcoded database connections found in application.properties"
fi

echo
echo "2. Checking HikariCpConfig.java for hardcoded values..."
if grep -n "localhost\|5432\|postgres" /Users/michaelwolf/fineract/fineract-provider/src/main/java/org/apache/fineract/infrastructure/core/config/HikariCpConfig.java; then
    echo "❌ Found hardcoded database values in HikariCpConfig.java"
else
    echo "✅ No hardcoded database values found in HikariCpConfig.java"
fi

echo
echo "3. Checking for environment variable usage..."
echo "Environment variables currently set:"
echo "DB_URL: $DB_URL"
echo "DB_USER: $DB_USER"  
echo "FINERACT_DEFAULT_TENANTDB_HOSTNAME: $FINERACT_DEFAULT_TENANTDB_HOSTNAME"
echo "FINERACT_DEFAULT_TENANTDB_PORT: $FINERACT_DEFAULT_TENANTDB_PORT"

echo
echo "4. Testing if DatabaseEnvironmentValidator compiles..."
if javac -cp "/Users/michaelwolf/fineract/fineract-provider/build/libs/*:/Users/michaelwolf/fineract/fineract-provider/src/main/java" \
    /Users/michaelwolf/fineract/fineract-provider/src/main/java/org/apache/fineract/infrastructure/core/config/DatabaseEnvironmentValidator.java 2>/dev/null; then
    echo "✅ DatabaseEnvironmentValidator compiles successfully"
else
    echo "❌ DatabaseEnvironmentValidator has compilation issues"
fi

echo
echo "5. Verifying configuration files..."
echo "Checking build.gradle database tasks..."
if grep -n "localhost\|5432" /Users/michaelwolf/fineract/fineract-provider/build.gradle | grep -v "System.getenv"; then
    echo "❌ Found hardcoded values in build.gradle database tasks"
else
    echo "✅ build.gradle uses environment variables for database tasks"
fi

echo
echo "===== Migration Verification Complete ====="
