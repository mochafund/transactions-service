package com.mochafund.transactionsservice.transaction.service;

import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.common.exception.ResourceNotFoundException;
import com.mochafund.transactionsservice.kafka.KafkaProducer;
import com.mochafund.transactionsservice.transaction.dto.CreateTransactionDto;
import com.mochafund.transactionsservice.transaction.dto.UpdateTransactionDto;
import com.mochafund.transactionsservice.transaction.entity.Transaction;
import com.mochafund.transactionsservice.transaction.events.TransactionEventPayload;
import com.mochafund.transactionsservice.transaction.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional(readOnly = true)
    public List<Transaction> listAllByWorkspaceId(UUID workspaceId) {
        return transactionRepository.findAllByWorkspaceId(workspaceId);
    }

    @Transactional(readOnly = true)
    public Transaction getTransaction(UUID workspaceId, UUID transactionId) {
        return transactionRepository.findByWorkspaceIdAndId(workspaceId, transactionId).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found"));
    }

    @Transactional
    public Transaction createTransaction(UUID userId, UUID workspaceId, CreateTransactionDto transactionDto) {
        Transaction transaction = CreateTransactionDto.fromDto(transactionDto);
        transaction.setWorkspaceId(workspaceId);
        transaction.setCreatedBy(userId);

        transaction = transactionRepository.save(transaction);

        kafkaProducer.send(EventEnvelope.<TransactionEventPayload>builder()
                .type(EventType.TAG_CREATED)
                .payload(TransactionEventPayload.builder()
                        .id(transaction.getId())
                        .workspaceId(workspaceId)
                        .accountId(transaction.getAccountId())
                        .categoryId(transaction.getCategoryId())
                        .date(transaction.getDate())
                        .amount(transaction.getAmount())
                        .status(transaction.getStatus())
                        .build())
                .build());

        log.info("Created transactionId={}", transaction.getId());
        return transaction;
    }

    @Transactional
    public Transaction updateTransaction(UUID workspaceId, UUID transactionId, UpdateTransactionDto transactionDto) {
        log.info("Updating transactionId={}", transactionId);

        Transaction transaction = this.getTransaction(workspaceId, transactionId);
        transaction.patchFrom(transactionDto);
        transaction = transactionRepository.save(transaction);

        kafkaProducer.send(EventEnvelope.<TransactionEventPayload>builder()
                .type(EventType.TAG_UPDATED)
                .payload(TransactionEventPayload.builder()
                        .id(transaction.getId())
                        .workspaceId(workspaceId)
                        .accountId(transaction.getAccountId())
                        .categoryId(transaction.getCategoryId())
                        .date(transaction.getDate())
                        .amount(transaction.getAmount())
                        .status(transaction.getStatus())
                        .build())
                .build());

        return transaction;
    }

    @Transactional
    public void deleteTransaction(UUID workspaceId, UUID transactionId) {
        Transaction transaction = this.getTransaction(workspaceId, transactionId);

        log.info("Deleting transactionId={}", transaction.getId());

        transactionRepository.deleteByWorkspaceIdAndId(transaction.getWorkspaceId(), transaction.getId());

        kafkaProducer.send(EventEnvelope.<TransactionEventPayload>builder()
                .type(EventType.TAG_DELETED)
                .payload(TransactionEventPayload.builder()
                        .id(transaction.getId())
                        .workspaceId(workspaceId)
                        .accountId(transaction.getAccountId())
                        .categoryId(transaction.getCategoryId())
                        .date(transaction.getDate())
                        .amount(transaction.getAmount())
                        .status(transaction.getStatus())
                        .build())
                .build());
    }
}
