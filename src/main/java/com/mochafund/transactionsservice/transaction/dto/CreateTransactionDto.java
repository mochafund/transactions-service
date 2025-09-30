package com.mochafund.transactionsservice.transaction.dto;

import com.mochafund.transactionsservice.transaction.entity.Transaction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDto {

    @NotNull(message = "Account ID must be provided")
    private UUID accountId;
    private UUID categoryId;
    private LocalDateTime date = LocalDateTime.now();
    private BigDecimal amount = BigDecimal.ZERO;

    public static Transaction fromDto(CreateTransactionDto transactionDto) {
        return Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .categoryId(transactionDto.getCategoryId())
                .date(transactionDto.getDate())
                .amount(transactionDto.getAmount())
                .build();
    }
}
