# Fineract → Rust/TS Migration Decisions

**Generated on:** $(date)  
**Migration Engine Version:** v1.0.0

## Technology Stack Decisions

### Backend Framework
- **Choice:** Axum v0.7.7 (latest stable)
- **Rationale:** Modern, performant async web framework with excellent ecosystem integration
- **Alternative considered:** actix-web (chose Axum for better ergonomics and tower ecosystem)

### Database & ORM
- **Database:** PostgreSQL 16.1 (matches existing Fineract requirement)
- **ORM:** Diesel v2.1.4 (latest stable)
- **Migration Tool:** Diesel CLI with embedded migrations
- **Rationale:** Type-safe ORM with excellent PostgreSQL support and compile-time query validation

### Async Runtime
- **Choice:** Tokio v1.40.0 (latest stable)
- **Rationale:** De facto standard for async Rust, excellent ecosystem support

### Frontend Stack
- **Framework:** Next.js v14.2.15 (App Router)
- **Language:** TypeScript v5.6.3
- **UI Library:** shadcn/ui (latest) with Radix UI primitives
- **Styling:** Tailwind CSS v3.4.13
- **State Management:** React Query v5.x for server state, Zustand for client state
- **Rationale:** Modern, type-safe, server-side rendering with excellent DX

### API & Documentation
- **API Style:** REST with OpenAPI 3.1.0 specification
- **Code Generation:** openapi-generator for client SDKs
- **Documentation:** Single source of truth in `/openapi/openapi.yaml`

### Infrastructure & DevOps
- **Containerization:** Docker with multi-stage builds
- **Orchestration:** docker-compose for development
- **CI/CD:** GitHub Actions (NO Railway as specified)
- **Package Management:** Cargo for Rust, npm for TypeScript

### Testing Strategy
- **Rust:** 
  - Unit tests: Built-in test framework
  - Integration tests: Testcontainers for database testing
  - Property testing: proptest for business logic validation
- **Frontend:**
  - Unit tests: Jest v29.x
  - Integration tests: Testing Library
  - E2E tests: Playwright v1.48.x

## Module Architecture Decisions

### Rust Crate Structure
```
backend/
├── Cargo.toml (workspace)
├── crates/
│   ├── fineract-core/          # Shared types, traits, utilities
│   ├── fineract-client/        # Client domain logic
│   ├── fineract-loan/          # Loan domain logic  
│   ├── fineract-savings/       # Savings domain logic
│   ├── fineract-accounting/    # Accounting domain logic
│   ├── fineract-reporting/     # Reporting domain logic
│   ├── fineract-api/           # HTTP API layer (Axum)
│   └── fineract-db/            # Database migrations & models
```

### Domain Boundaries
- **Client Management:** Client CRUD, KYC, documentation
- **Loan Management:** Loan products, applications, disbursement, repayment
- **Savings Management:** Savings products, accounts, deposits, withdrawals
- **Accounting:** Chart of accounts, journal entries, trial balance
- **Reporting:** Standard reports, custom reports, dashboards
- **Core:** Authentication, authorization, audit, configuration

## Database Schema Decisions

### Migration Strategy
- Convert existing MySQL/PostgreSQL schema to Diesel migrations
- Maintain data compatibility where possible
- Normalize redundant data structures found in original schema
- Add proper foreign key constraints and indexes

### Naming Conventions
- **Tables:** snake_case (e.g., `loan_accounts`, `client_details`)
- **Columns:** snake_case matching Rust field names
- **Indexes:** `idx_{table}_{column(s)}`
- **Foreign Keys:** `fk_{table}_{referenced_table}`

## Code Style & Conventions

### Rust
- **Formatting:** rustfmt with default settings
- **Linting:** clippy with pedantic lints enabled
- **Error Handling:** Custom error types with thiserror
- **Async:** async/await throughout, no blocking operations in async contexts
- **Serialization:** serde with snake_case field naming

### TypeScript
- **Formatting:** Prettier with 2-space indents
- **Linting:** ESLint with TypeScript recommended rules
- **Naming:** camelCase for variables/functions, PascalCase for types/components
- **Import Style:** Absolute imports with path mapping

## Migration Assumptions

### Business Logic Preservation
- All existing REST endpoints will be preserved with same paths
- Request/response formats maintained for backward compatibility
- Business rules and validation logic ported exactly
- Audit trail and history preservation maintained

### Performance Requirements
- Target: <100ms for simple CRUD operations
- Target: <500ms for complex business operations
- Target: Support 1000+ concurrent users
- Database connection pooling: 10-50 connections per instance

### Security Assumptions
- Existing authentication mechanisms will be preserved
- Role-based access control (RBAC) maintained
- API rate limiting implemented
- Input validation on all endpoints
- SQL injection prevention via parameterized queries

### Data Migration
- Existing data will be migrated via ETL scripts
- No data loss acceptable
- Downtime window: <4 hours for migration
- Rollback plan required for each migration step

## Excluded/Deprecated Features

### Java-Specific Features to Remove
- Spring Boot auto-configuration magic
- JPA/Hibernate lazy loading patterns
- Java-specific batch processing
- JSP/servlet-based UI components

### Apache License Cleanup
- Remove Apache license headers from generated code
- Clean up NOTICE files
- Remove dependency on Apache-specific libraries where possible
- Maintain compliance with Apache 2.0 for derived work

## Development Workflow Decisions

### Git Strategy
- Feature branches with PR reviews
- Conventional commits for changelog generation
- Semantic versioning for releases
- No direct commits to main branch

### Environment Management
- `.env.example` with all required variables documented
- Environment-specific config files
- Docker Compose profiles for different environments
- Configuration validation on startup

### Monitoring & Observability
- Structured logging with tracing spans
- Metrics collection with Prometheus format
- Health check endpoints
- Database query performance monitoring

---

**Note:** This document will be updated throughout the migration process as decisions are refined or changed.