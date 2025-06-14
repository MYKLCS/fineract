#!/bin/bash
# Test environment variables for PostgreSQL migration verification

# Database configuration
export DB_URL="jdbc:postgresql://localhost:5432/fineract_tenants"
export DB_USER="postgres"
export DB_PASSWORD="postgres"

# Tenant database configuration
export FINERACT_DEFAULT_TENANTDB_HOSTNAME="localhost"
export FINERACT_DEFAULT_TENANTDB_PORT="5432"
export FINERACT_DEFAULT_TENANTDB_UID="postgres"
export FINERACT_DEFAULT_TENANTDB_PWD="postgres"

# Optional tenant configuration
export FINERACT_DEFAULT_TENANTDB_CONN_PARAMS=""
export FINERACT_DEFAULT_TENANTDB_TIMEZONE="Asia/Kolkata"
export FINERACT_DEFAULT_TENANTDB_IDENTIFIER="default"
export FINERACT_DEFAULT_TENANTDB_NAME="fineract_default"
export FINERACT_DEFAULT_TENANTDB_DESCRIPTION="Default Demo Tenant"
export FINERACT_DEFAULT_TENANTDB_MASTER_PASSWORD="fineract"
export FINERACT_DEFAULT_TENANTDB_ENCRYPTION="AES/CBC/PKCS5Padding"

# Node ID
export FINERACT_NODE_ID="1"

echo "Test environment variables set for PostgreSQL migration verification"
echo "DB_URL: $DB_URL"
echo "TENANT_DB_HOST: $FINERACT_DEFAULT_TENANTDB_HOSTNAME"
echo "TENANT_DB_PORT: $FINERACT_DEFAULT_TENANTDB_PORT"
