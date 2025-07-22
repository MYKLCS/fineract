# Architectural Decisions Log

This log captures the key decisions taken during the Rust/React migration of the Fineract code-base.  Revisit only through PR review.

## 2025-07-22 — Initial Stack Selection

### Backend
1. **Programming Language**: Rust 1.74 (stable).
2. **HTTP Framework**: `axum` 0.7 with `tower` ecosystem for middleware.
3. **ORM / Query**: `diesel` 2.1 (> PostgreSQL only), code-gen enabled.
4. **Async Runtime**: `tokio` 1.37.
5. **Authentication**: `jsonwebtoken` + `argon2` for password hashes, pluggable OAuth2.
6. **OpenAPI**: `utoipa` for code-first spec generation.
7. **Validation**: `validator` + custom derive macros.
8. **Messaging**: `rdkafka` + `serde_json` + `schemars` for schema-checked events (replaces Avro).

### Database
1. **RDBMS**: PostgreSQL 15 (single writer; patroni/replicas in prod).
2. **Migrations**: `diesel_cli` migrations folder, mirrored in `/db/migrations` crate.
3. **Schema Namespacing**: single `public` schema; table prefixes per domain: `acct_`, `loan_`, etc.
4. **Temporal Columns**: UTC `TIMESTAMPTZ`; soft-delete via `deleted_at` nullable column.

### Frontend
1. **Bundler**: Vite 5.
2. **Framework**: React 18 + TypeScript 5.
3. **Routing**: React Router v6 (file-based via `vite-plugin-pages`).
4. **State**: TanStack Query v5 + Zustand for local state.
5. **UI**: TailwindCSS + Headless UI.
6. **API Client**: Generated via `openapi-typescript`.

### Testing Strategy
1. **Backend**: `cargo test` unit, `cucumber` BDD, `insta` snapshots.
2. **Frontend**: Vitest + Testing Library; Cypress e2e.
3. **Contract**: `pact` consumer/provider tests for Kafka topics.

### Coding Conventions
- `snake_case` for Rust modules; `CamelCase` for types.
- One domain = one crate under `crates/`.
- Service traits live in `domain` crate; Axum handlers call trait objects.
- Public APIs return `Result<T, ApiError>`; errors map to RFC 7807.

### Module Naming
- `crates/common`  — cross-cutting helpers.
- `crates/db`      — Diesel schema & helpers.
- `crates/<domain>` — domain logic (`clients`, `loans`, `savings`, ...).
- `crates/api`     — Axum router compositions.

### CI & Quality Gates
- GitHub Actions: build matrix Linux/macOS, PR checks.
- `cargo clippy --deny warnings`.
- `cargo fmt --check`.
- `wasm-pack` build for frontend.
- `eslint` + `prettier` + `tsc --noEmit`.

### Containerization
- Multistage Dockerfiles: `rust:slim` → `scratch`.
- Render.com & k8s Helm charts.

### Reasoning Summary
Rust offers memory safety, Diesel provides compile-time SQL assurances, and Axum aligns with Tokio's async ecosystem.  Vue/React considered; React chosen for community size and TS support.

### Initial PostgreSQL Schema Outline
```
-- Clients & Branches
CREATE TABLE branch (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    parent_id INTEGER REFERENCES branch(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    branch_id INTEGER REFERENCES branch(id),
    external_id TEXT UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    kyc_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Accounting (simplified)
CREATE TABLE coa_account (
    id SERIAL PRIMARY KEY,
    type SMALLINT NOT NULL, -- 0=Asset 1=Liability ...
    name TEXT NOT NULL,
    parent_id INTEGER REFERENCES coa_account(id)
);
CREATE TABLE journal_entry (
    id BIGSERIAL PRIMARY KEY,
    tx_date DATE NOT NULL,
    description TEXT,
    posted_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE journal_entry_line (
    id BIGSERIAL PRIMARY KEY,
    journal_entry_id BIGINT REFERENCES journal_entry(id),
    account_id INTEGER REFERENCES coa_account(id),
    amount NUMERIC(19,4) NOT NULL,
    is_debit BOOLEAN NOT NULL
);

-- Savings
CREATE TABLE savings_account (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES client(id),
    product_code TEXT NOT NULL,
    balance NUMERIC(19,4) DEFAULT 0,
    interest_rate NUMERIC(5,2),
    status SMALLINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Loans
CREATE TABLE loan_account (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES client(id),
    principal NUMERIC(19,4) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL,
    term INTEGER NOT NULL,
    repayment_every SMALLINT NOT NULL,
    status SMALLINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE TABLE loan_repayment_schedule (
    id SERIAL PRIMARY KEY,
    loan_account_id INTEGER REFERENCES loan_account(id),
    installment_number SMALLINT,
    due_date DATE NOT NULL,
    principal_due NUMERIC(19,4),
    interest_due NUMERIC(19,4)
);
```

The full schema will evolve during migration; see `/crates/db/migrations` for canonical DDL.