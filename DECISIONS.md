# DECISIONS LOG

_A living document recording every architectural and tooling decision made during the Fineract → Rust/TypeScript rewrite. Update chronologically – newest at top._

---

## 2025-07-22 — Phase 0 Kick-off

### Stack Selection

| Concern | Decision | Rationale |
|---------|----------|-----------|
| Web framework | **Axum 0.8.4** | Tokio-native, minimal, modular, middleware-friendly, maturer than alternatives (Actix Web heavy, Warp stagnant). |
| Async runtime | **Tokio 1.44** | Ecosystem standard; Diesel async adapter is Tokio-based. |
| ORM | **Diesel 2.2.12** | Compile-time safety, mature Postgres support, generates Migrations. |
| Database | **PostgreSQL 16 (via Docker)** | Fineract already migrated from MySQL; keep Postgres, leverage its advanced types & JSON. |
| Message broker | **None initial** | Fineract's original ActiveMQ/Kafka usage optional; revisit after MVP. |
| Build & CI | **GitHub Actions** | Free OSS runners, matrix for Rust, Node, Playwright. |
| Containerization | **Docker + docker-compose** | Simplest local onboarding; Kubernetes manifests deferred. |
| Frontend framework | **Next.js 15.4 (App Router)** | React ecosystem, server components, built-in router & API routes, easy Vercel/CDN deploy; App Router future-proof. |
| Frontend styling | **shadcn/ui + Tailwind 4** | Accessible headless components, matches Next.js community. |
| Language versions | Rust 1.77 (stable), TypeScript 5.8.3, Node 20.19 (LTS) | Latest stable, recorded here to freeze builds. |
| Testing (backend) | **cargo-nextest + rstest + sqlx-test** | Parallelism & flaky-test detection. |
| Testing (frontend) | **Jest + Testing Library + Playwright** | Unit + integration + E2E coverage. |
| JSON Validation | **Serde with `utoipa` + Zod on frontend** | Generate OpenAPI 3 spec and share schemas. |
| Docs platform | Markdown + mdBook + typedoc | Easy GitHub Pages hosting; no Confluence. |

### Coding Conventions

* Rust: 2021 edition, `rustfmt.toml` with `imports_granularity = "Crate"`, Clippy `#![deny(warnings, clippy::all)]`.
* Workspace uses `cargo deny` to audit licenses & vulns.
* Crate names: kebab-case for binaries (`api`), snake_case for libraries (`loans`).
* Files: `mod.rs` discouraged; use explicit `mod xyz;` in `lib.rs`.
* All Diesel models derive `Serialize`, `Deserialize`, `Debug`, `Clone` – no heavy ORM entities.
* OpenAPI generated via `utoipa::OpenApi` derive; served at `/openapi.json` and interactive ReDoc UI.
* Frontend uses **strict typed routes** (`typed-routes` experimental flag) and ESLint strictest preset.
* Absolute imports via TS baseUrl `@/*`.

### Project Layout

```
/
├─ backend/
│   ├─ crates/
│   └─ api/
├─ frontend/
│   └─ app/
├─ docker/
│   ├─ postgres/
│   └─ dev.Dockerfile
└─ .github/workflows/
```

### Assumptions

1. We will drop Apache license headers (rewrite constitutes new codebase under MIT/Apache-2.0 dual license).
2. Progressive-loan embeddable schedule generator will be re-implemented from scratch; old Java algo used as reference only.
3. Reporting will rely on Postgres materialized views + Supabase dashboards; Pentaho *.prpt output removed.
4. Multi-tenant support kept by scoping every table with `tenant_id` (UUID) & row-level security.
5. Internationalization stored in JSONB translation columns; server returns i18next resources.

### Pending

* Message queue decision for async jobs – revisit after accounting module.
* Webhook/eventing strategy (Kafka vs NATS) – record later.

---