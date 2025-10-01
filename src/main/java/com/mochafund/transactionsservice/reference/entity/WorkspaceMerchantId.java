package com.mochafund.transactionsservice.reference.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WorkspaceMerchantId implements Serializable {

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;
}
