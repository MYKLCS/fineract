| Module | Purpose | Key Classes (examples) | Dependencies | Decision |
|--------|---------|------------------------|--------------|----------|
| fineract-core | Shared kernel abstractions: data types, utilities, platform constants, localization, error handling | PlatformService, JsonHelper, CommandWrapper | None (foundation) | REWRITE (migrate essential utilities to Rust common crate) |
| fineract-db | Liquibase migrations & DB tooling | *Liquibase changelogs* | fineract-core | KEEP (translate to Diesel migrations) |
| fineract-provider | REST controllers, request/response DTOs, authentication filters | *LoanAccountRestController*, *ClientRestController* | core, loan, savings, accounting, charge, report | REWRITE (into Axum routers) |
| fineract-loan | Loan domain logic, amortization schedules, repayment, charges | Loan, LoanRepaymentScheduleInstallment, LoanAssembler | core, accounting, charge, rates | REWRITE |
| fineract-savings | Savings accounts, interest posting, transactions | SavingsAccount, SavingsTransaction | core, accounting, charge | REWRITE |
| fineract-client | Client onboarding & KYC | Client, ClientDataValidator | core | REWRITE |
| fineract-accounting | Double-entry GL, journal entries, COA | JournalEntry, GeneralLedgerAccount | core | REWRITE |
| fineract-charge | Dynamic fee definitions & application | Charge, ChargeApplicabilityChecker | core | REWRITE |
| fineract-rates | Interest rate charts | Rate, RateAssembler | core | DROP (merge into loan/savings) |
| fineract-branch | Branch / office hierarchy | Office, OfficeHierarchy | core | KEEP (rewrite minimal) |
| fineract-report | Pentaho reporting integration | ReportingService | provider, db | DROP (replace with SQL views & BI) |
| fineract-investor | Loan syndication & investor settlement | LoanPayoff, InvestorInfo | loan, accounting | DROP (out-of-scope) |
| fineract-progressive-loan | Progressive disbursement loan logic | ProgressiveLoan, ScheduleGenerator | loan | REWRITE |
| fineract-progressive-loan-embeddable-schedule-generator | Schedule generator lib used by progressive loan | EmbeddableScheduleGenerator | progressive-loan | KEEP (rewrite in Rust) |
| fineract-document | Document management (upload, templates) | DocumentManagementService | provider, core | KEEP (rewrite) |
| fineract-tax | Tax configuration & calculation | TaxComponent, TaxGroup | core, accounting | REWRITE |
| fineract-avro-schemas | Kafka message schemas | Avro schema files (.avsc) | None | KEEP (regenerate with schemars) |
| fineract-command | Command pattern audit log | CommandSource, CommandWrapper | core | DROP (use generic event audit) |
| fineract-war | Servlet WAR packaging, Spring Boot main app | FineractServerApplication | all | DROP (Axum replaces) |
| integration-tests, e2e-tests, oauth2-tests, twofactor-tests | Automated tests | Spock/JUnit test classes | all | REWRITE (Rust + Cypress) |