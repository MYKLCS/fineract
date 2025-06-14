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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("#{ systemEnvironment['fineract_tenants_driver'] == null }")
public class HikariCpConfig {

    @Value("${DB_URL}")
    private String jdbcUrl;

    @Value("${DB_USER}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    // initMethod is triggering lazy initialization of Hikari pool
    @Bean(initMethod = "getConnection", destroyMethod = "close")
    public HikariDataSource hikariTenantDataSource() {
        HikariConfig config = new HikariConfig();
        
        // Set connection parameters from environment variables
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        
        // Set connection pool settings
        config.setMinimumIdle(3);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(20000);
        config.setConnectionTestQuery("SELECT 1");
        config.setAutoCommit(true);
        
        // PostgreSQL-specific optimizations
        config.addDataSourceProperty("prepareThreshold", "5");
        config.addDataSourceProperty("preparedStatementCacheQueries", "256");
        config.addDataSourceProperty("preparedStatementCacheSizeMiB", "5");
        config.addDataSourceProperty("defaultRowFetchSize", "1000");
        config.addDataSourceProperty("logUnclosedConnections", "true");
        
        return new HikariDataSource(config);
    }
}
