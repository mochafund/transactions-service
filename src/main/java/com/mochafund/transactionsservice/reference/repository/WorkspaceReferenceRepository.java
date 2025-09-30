package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceReferenceRepository extends JpaRepository<WorkspaceReference, UUID> {
}
