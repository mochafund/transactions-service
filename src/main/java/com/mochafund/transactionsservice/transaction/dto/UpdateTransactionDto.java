package com.mochafund.transactionsservice.transaction.dto;

import com.mochafund.transactionsservice.transaction.enums.TransactionStatus;
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
public class UpdateTransactionDto {

    private UUID accountId;
    private UUID merchantId;
    private UUID categoryId;
    private LocalDateTime date;
    private BigDecimal amount;
    private TransactionStatus status;
}
