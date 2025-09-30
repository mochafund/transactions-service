package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceCategoryRepository extends JpaRepository<WorkspaceCategoryReference, WorkspaceCategoryId> {

    void deleteByIdWorkspaceId(UUID workspaceId);

    boolean existsByIdWorkspaceIdAndIdCategoryId(UUID workspaceId, UUID categoryId);
}
