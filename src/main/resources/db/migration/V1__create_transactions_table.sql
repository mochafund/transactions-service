-- Create transactions table
CREATE TABLE transactions (
      id UUID PRIMARY KEY,
      created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      created_by UUID NOT NULL,
      workspace_id UUID NOT NULL,
      account_id UUID NOT NULL,
      category_id UUID,
      date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
      amount NUMERIC(19, 2) NOT NULL,
      status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'ARCHIVED'))
);
