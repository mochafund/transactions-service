package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceTagId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceTagReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceTagRepository extends JpaRepository<WorkspaceTagReference, WorkspaceTagId> {
}
