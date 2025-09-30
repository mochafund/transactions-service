package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceCategoryRepository extends JpaRepository<WorkspaceCategoryReference, WorkspaceCategoryId> {
}
