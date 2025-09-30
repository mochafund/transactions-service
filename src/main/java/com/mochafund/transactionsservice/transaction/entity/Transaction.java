package com.mochafund.transactionsservice.transaction.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mochafund.transactionsservice.common.annotations.PatchableField;
import com.mochafund.transactionsservice.common.entity.BaseEntity;
import com.mochafund.transactionsservice.common.patchable.Patchable;
import com.mochafund.transactionsservice.transaction.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DynamicUpdate
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions")
public class Transaction extends BaseEntity implements Patchable {

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @PatchableField
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @PatchableField
    @Column(name = "category_id")
    private UUID categoryId;

    @PatchableField
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "date", nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @PatchableField
    @Builder.Default
    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @PatchableField
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.ACTIVE;
}
