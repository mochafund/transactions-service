CREATE TABLE workspaces (
    id UUID PRIMARY KEY,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE workspace_accounts (
    workspace_id UUID NOT NULL,
    account_id UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (workspace_id, account_id),
    CONSTRAINT fk_workspace_accounts_workspace
        FOREIGN KEY (workspace_id)
        REFERENCES workspaces(id)
        ON DELETE CASCADE
);

CREATE TABLE workspace_categories (
    workspace_id UUID NOT NULL,
    category_id UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (workspace_id, category_id),
    CONSTRAINT fk_workspace_categories_workspace
        FOREIGN KEY (workspace_id)
        REFERENCES workspaces(id)
        ON DELETE CASCADE
);

CREATE TABLE workspace_tags (
    workspace_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (workspace_id, tag_id),
    CONSTRAINT fk_workspace_tags_workspace
        FOREIGN KEY (workspace_id)
        REFERENCES workspaces(id)
        ON DELETE CASCADE
);

INSERT INTO workspaces (id)
SELECT DISTINCT workspace_id
FROM transactions;

ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_workspace
    FOREIGN KEY (workspace_id)
    REFERENCES workspaces(id)
    ON DELETE CASCADE;
