package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceAccountId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceAccountReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceAccountRepository extends JpaRepository<WorkspaceAccountReference, WorkspaceAccountId> {

    void deleteByIdWorkspaceId(UUID workspaceId);

    boolean existsByIdWorkspaceIdAndIdAccountId(UUID workspaceId, UUID accountId);
}
