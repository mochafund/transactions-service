package com.mochafund.transactionsservice.reference.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.common.util.CorrelationIdUtil;
import com.mochafund.transactionsservice.reference.consumers.payloads.MerchantEventPayload;
import com.mochafund.transactionsservice.reference.service.WorkspaceMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantEventConsumer {

    private static final String GROUP_ID = "transactions-service";
    private final ObjectMapper objectMapper;
    private final WorkspaceMetadataService metadataService;

    @KafkaListener(topics = EventType.MERCHANT_CREATED, groupId = GROUP_ID)
    public void handleMerchantCreated(String message) {
        EventEnvelope<MerchantEventPayload> event = this.readEnvelope(message, MerchantEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            MerchantEventPayload payload = event.getPayload();
            log.info("Processing merchant.created - Merchant: {}", payload.getId());
            metadataService.upsertMerchant(payload);
        });
    }

    @KafkaListener(topics = EventType.MERCHANT_DELETED, groupId = GROUP_ID)
    public void handleMerchantDeleted(String message) {
        EventEnvelope<MerchantEventPayload> event = readEnvelope(message, MerchantEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            MerchantEventPayload payload = event.getPayload();
            log.info("Processing merchant.deleted - Merchant: {}", payload.getId());
            metadataService.removeMerchant(payload);
        });
    }

    private <T> EventEnvelope<T> readEnvelope(String message, Class<T> payloadType) {
        try {
            return objectMapper.readValue(
                    message,
                    objectMapper.getTypeFactory().constructParametricType(EventEnvelope.class, payloadType)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to parse event envelope", e);
        }
    }
}
