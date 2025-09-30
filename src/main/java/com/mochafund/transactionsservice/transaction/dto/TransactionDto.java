package com.mochafund.transactionsservice.transaction.dto;

import com.mochafund.transactionsservice.common.dto.BaseDto;
import com.mochafund.transactionsservice.transaction.entity.Transaction;
import com.mochafund.transactionsservice.transaction.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TransactionDto extends BaseDto {

    private UUID workspaceId;
    private UUID accountId;
    private UUID categoryId;
    private UUID createdBy;
    private LocalDateTime date;
    private BigDecimal amount;
    private TransactionStatus status;

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .workspaceId(transaction.getWorkspaceId())
                .accountId(transaction.getAccountId())
                .categoryId(transaction.getCategoryId())
                .createdBy(transaction.getCreatedBy())
                .date(transaction.getDate())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .build();
    }

    public static List<TransactionDto> fromEntities(List<Transaction> transactions) {
        return transactions.stream().map(TransactionDto::fromEntity).toList();
    }
}
