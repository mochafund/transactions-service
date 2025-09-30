package com.mochafund.transactionsservice.transaction.service;

import com.mochafund.transactionsservice.transaction.dto.CreateTransactionDto;
import com.mochafund.transactionsservice.transaction.dto.UpdateTransactionDto;
import com.mochafund.transactionsservice.transaction.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    Transaction createTransaction(UUID userId, UUID workspaceId, CreateTransactionDto transactionDto);
    Transaction getTransaction(UUID workspaceId, UUID transactionId);
    Transaction updateTransaction(UUID workspaceId, UUID transactionId, UpdateTransactionDto transactionDto);
    void deleteTransaction(UUID workspaceId, UUID transactionId);
    List<Transaction> listAllByWorkspaceId(UUID workspaceId);
}
