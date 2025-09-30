package com.mochafund.transactionsservice.transaction.repository;

import com.mochafund.transactionsservice.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByWorkspaceIdAndId(UUID workspaceId, UUID transactionId);
    List<Transaction> findAllByWorkspaceId(UUID workspaceId);
    void deleteByWorkspaceIdAndId(UUID workspaceId, UUID transactionId);
    boolean existsByWorkspaceId(UUID workspaceId);
}
