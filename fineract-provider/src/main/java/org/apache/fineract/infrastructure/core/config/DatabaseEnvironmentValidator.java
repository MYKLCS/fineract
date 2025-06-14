/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.core.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseEnvironmentValidator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseEnvironmentValidator.class);

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USER}")
    private String dbUser;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${FINERACT_DEFAULT_TENANTDB_HOSTNAME}")
    private String tenantDbHost;

    @Value("${FINERACT_DEFAULT_TENANTDB_PORT}")
    private String tenantDbPort;

    @Value("${FINERACT_DEFAULT_TENANTDB_UID}")
    private String tenantDbUser;

    @Value("${FINERACT_DEFAULT_TENANTDB_PWD}")
    private String tenantDbPassword;

    @PostConstruct
    public void validateDatabaseConfiguration() {
        log.info("Validating database environment configuration...");
        
        validateRequired("DB_URL", dbUrl);
        validateRequired("DB_USER", dbUser);
        validateRequired("DB_PASSWORD", dbPassword);
        validateRequired("FINERACT_DEFAULT_TENANTDB_HOSTNAME", tenantDbHost);
        validateRequired("FINERACT_DEFAULT_TENANTDB_PORT", tenantDbPort);
        validateRequired("FINERACT_DEFAULT_TENANTDB_UID", tenantDbUser);
        validateRequired("FINERACT_DEFAULT_TENANTDB_PWD", tenantDbPassword);
        
        // Validate PostgreSQL URL format
        if (!dbUrl.startsWith("jdbc:postgresql://")) {
            throw new IllegalStateException("DB_URL must be a valid PostgreSQL JDBC URL starting with 'jdbc:postgresql://'");
        }
        
        log.info("Database environment validation successful");
        log.info("Main datasource URL: {}", maskCredentials(dbUrl));
        log.info("Tenant database host: {}:{}", tenantDbHost, tenantDbPort);
    }

    private void validateRequired(String propertyName, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(String.format(
                "Required environment variable '%s' is not set. " +
                "All database configuration must be provided via environment variables.", 
                propertyName));
        }
    }

    private String maskCredentials(String url) {
        // Simple masking for logging - replace anything that looks like credentials
        return url.replaceAll("://[^/]+@", "://***:***@");
    }
}
