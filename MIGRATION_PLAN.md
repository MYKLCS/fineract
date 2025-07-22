# Migration Plan

> This file is updated continuously during the migration.  **Do not edit completed items unless strictly necessary.**  Keep the ordering – later tasks may depend on earlier ones.

Legend:
- [ ] TODO  
- [/] In-Progress  
- [x] Done

## Global Bootstrap

| # | Task | Depends On | Status |
|---|------|------------|--------|
| 0 | Scaffold Rust workspace (`cargo new --workspace fineract-rs`) with CI (rustfmt/clippy) | – | [ ] |
| 1 | Add `common` crate (error types, shared utils, validation) | 0 | [ ] |
| 2 | Add `db` crate (Diesel, connection pool, migrations harness) | 1 | [ ] |
| 3 | Configure Docker compose for Postgres 15 + `sqlx-cli`, wire into CI | 2 | [ ] |
| 4 | Create `api` crate (Axum server skeleton, OpenAPI generator) | 1 | [ ] |

## Domain Migration

| # | Domain | Task | Depends On | Status |
|----|--------|------|------------|--------|
| 10 | Accounting | Port COA & Journal Entry schema + service layer | 2 | [ ] |
| 11 | Accounting | Implement double-entry posting engine tests | 10 | [ ] |
| 20 | Clients | Create `clients` crate: CRUD endpoints, KYC validation | 4,10 | [ ] |
| 30 | Branch | Implement office/branch hierarchy tables & services | 2 | [ ] |
| 40 | Charges | Model charge definitions & application rules | 10 | [ ] |
| 50 | Savings | Savings account schema, interest posting cron | 10,20,40 | [ ] |
| 60 | Loans | Loan schema, amortization schedule, repayment | 10,20,40,50 | [ ] |
| 61 | Progressive-Loans | Implement progressive disbursement extension | 60 | [ ] |
| 70 | Tax | Tax components & grouping logic | 10,20 | [ ] |
| 80 | Documents | File storage & template rendering microservice | 1 | [ ] |

## Cross-Cutting & Integrations

| # | Task | Depends On | Status |
|----|------|------------|--------|
| 90 | Replace Avro schemas with `schemars` + `serde_json` for Kafka topics | 4 | [ ] |
| 91 | Replace Pentaho reports with SQL views + Metabase dashboards | 10-70 | [ ] |

## Frontend React App

| # | Page / Route | Depends On | Status |
|----|--------------|------------|--------|
| F0 | Vite + React + TS + Tailwind scaffold | – | [ ] |
| F1 | Login / 2FA flow | F0 | [ ] |
| F2 | Client list / detail / onboarding wizard | F1,20 | [ ] |
| F3 | Savings account list / detail | F2,50 | [ ] |
| F4 | Loan account list / detail / repayment | F2,60 | [ ] |
| F5 | Accounting journal & COA explorer | F1,10 | [ ] |

## Documentation & Ops

| # | Task | Depends On | Status |
|----|------|------------|--------|
| D0 | Update OpenAPI spec as each endpoint stabilises | 4+ | [ ] |
| D1 | Add README sections for local dev & Docker | 0 | [ ] |
| D2 | Add Grafana/Loki/Prometheus stack for observability | 0 | [ ] |
| D3 | Add k8s manifests & Helm charts | 0 | [ ] |

---

Generated on: $(date)