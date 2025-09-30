# Transactions Service Roadmap

## Domain focus
- Owns all financial transaction records for a workspace.
- Stores references to workspace-service metadata (accounts, categories, tags) via UUIDs.
- Never mutates workspace metadata directly; emits domain events back to workspace-service for balance updates or other projections.

## Data model
- `transactions` table (one row per transaction header):
  - `id`, `workspace_id`, `account_id`, `amount`, `direction`, `posted_at`, `description`, `notes`, audit fields.
  - Optional external identifiers for import/idempotency support.
- `transaction_splits` table (child rows):
  - `id`, `transaction_id`, `category_id`, `amount`, optional `memo`/`tag` relationships.
  - Tag linkage via join table (`transaction_split_tags`) to support multiple tags per split.
- Projection tables sourced from workspace-service events:
  - `workspaces`, `workspace_accounts`, `workspace_categories`, `workspace_tags` (all keyed by `(workspace_id, entity_id)` with `ON DELETE CASCADE`).
- Enforce referential integrity on `workspace_id` and account/category/tag UUIDs using the local projections before persisting.

## Event-driven integration
- Consume `workspace.deleted` (and potentially `account/category/tag` deletion) events to cascade delete or archive related transactions.
- Publish `transaction.created`, `transaction.updated`, `transaction.deleted` events carrying `workspaceId`, `accountId`, net amount delta, and transaction metadata so workspace-service can update balances asynchronously.
- Ensure events include idempotency keys (`transactionId` + revision) for safe retries.

## API surface (initial)
- CRUD endpoints for transactions (single create/update/delete).
- Optional split management endpoints if granular adjustments are needed separately.
- List endpoints scoped by `workspaceId`, with filters for account, category, tag, date range.

## Validation & safeguards
- On writes, validate referenced account/category/tag UUIDs exist and are active by calling workspace-service (or cached metadata).
- Prevent assigning transactions to archived accounts/categories unless explicitly allowed.
- Guard against split totals not matching transaction amount.

## Future considerations
- Support bulk ingestion/import flows with idempotency and batching.
- Derive running balances or cached rollups per account if performance requires it.
- Add attachment support (receipts) via a storage service.
- Integrate with reconciliation workflows (cleared vs pending states).
