package com.mochafund.transactionsservice.reference.repository;

import com.mochafund.transactionsservice.reference.entity.WorkspaceMerchantId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceMerchantReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMerchantRepository extends JpaRepository<WorkspaceMerchantReference, WorkspaceMerchantId> {
}
