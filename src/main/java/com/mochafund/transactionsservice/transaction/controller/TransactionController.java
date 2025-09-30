package com.mochafund.transactionsservice.transaction.controller;

import com.mochafund.transactionsservice.common.annotations.UserId;
import com.mochafund.transactionsservice.common.annotations.WorkspaceId;
import com.mochafund.transactionsservice.transaction.dto.CreateTransactionDto;
import com.mochafund.transactionsservice.transaction.dto.TransactionDto;
import com.mochafund.transactionsservice.transaction.dto.UpdateTransactionDto;
import com.mochafund.transactionsservice.transaction.entity.Transaction;
import com.mochafund.transactionsservice.transaction.service.ITransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final ITransactionService transactionService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionDto>> getAllTransactions(@WorkspaceId UUID workspaceId) {
        List<Transaction> transactions = transactionService.listAllByWorkspaceId(workspaceId);
        return ResponseEntity.ok().body(TransactionDto.fromEntities(transactions));
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> getTransaction(@WorkspaceId UUID workspaceId, @PathVariable UUID transactionId) {
        Transaction transaction = transactionService.getTransaction(workspaceId, transactionId);
        return ResponseEntity.ok().body(TransactionDto.fromEntity(transaction));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> createTransaction(
            @UserId UUID userId, @WorkspaceId UUID workspaceId,
            @Valid @RequestBody CreateTransactionDto transactionDto
    ) {
        Transaction transaction = transactionService.createTransaction(userId, workspaceId, transactionDto);
        return ResponseEntity.status(201).body(TransactionDto.fromEntity(transaction));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> updateTransaction(
            @WorkspaceId UUID workspaceId, @PathVariable UUID transactionId,
            @Valid @RequestBody UpdateTransactionDto transactionDto) {
        Transaction transaction = transactionService.updateTransaction(workspaceId, transactionId, transactionDto);
        return ResponseEntity.ok().body(TransactionDto.fromEntity(transaction));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTransaction(@WorkspaceId UUID workspaceId, @PathVariable UUID transactionId) {
        transactionService.deleteTransaction(workspaceId, transactionId);
        return ResponseEntity.noContent().build();
    }
}
