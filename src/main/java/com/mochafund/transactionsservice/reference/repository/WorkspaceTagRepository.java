package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceTagId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceTagReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceTagRepository extends JpaRepository<WorkspaceTagReference, WorkspaceTagId> {

    void deleteByIdWorkspaceId(UUID workspaceId);

    boolean existsByIdWorkspaceIdAndIdTagId(UUID workspaceId, UUID tagId);
}
