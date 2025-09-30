package com.mochafund.transactionsservice.transaction.dto;

import com.mochafund.transactionsservice.transaction.entity.Transaction;
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

    private UUID accountId;
    private UUID categoryId;
    private LocalDateTime date;
    private BigDecimal amount;

    public static Transaction fromDto(CreateTransactionDto transactionDto) {
        return Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .categoryId(transactionDto.getCategoryId())
                .date(transactionDto.getDate())
                .amount(transactionDto.getAmount())
                .build();
    }
}
