package com.mochafund.transactionsservice.transaction.events;

import com.mochafund.transactionsservice.transaction.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionEventPayload {
    private UUID id;
    private UUID workspaceId;
    private UUID accountId;
    private UUID categoryId;
    private LocalDateTime date;
    private BigDecimal amount;
    private TransactionStatus status;
}
