package com.mochafund.transactionsservice.reference.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.reference.consumers.payloads.CategoryEventPayload;
import com.mochafund.transactionsservice.common.util.CorrelationIdUtil;
import com.mochafund.transactionsservice.reference.service.WorkspaceMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryEventConsumer {

    private static final String GROUP_ID = "transactions-service";
    private final ObjectMapper objectMapper;
    private final WorkspaceMetadataService metadataService;

    @KafkaListener(topics = EventType.CATEGORY_CREATED, groupId = GROUP_ID)
    public void handleCategoryCreated(String message) {
        EventEnvelope<CategoryEventPayload> event = readEnvelope(message, CategoryEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            CategoryEventPayload payload = event.getPayload();
            log.info("Processing category.created - Category: {}", payload.getId());
            metadataService.upsertCategory(payload);
        });
    }

    @KafkaListener(topics = EventType.CATEGORY_DELETED, groupId = GROUP_ID)
    public void handleCategoryDeleted(String message) {
        EventEnvelope<CategoryEventPayload> event = readEnvelope(message, CategoryEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            CategoryEventPayload payload = event.getPayload();
            log.info("Processing category.deleted - Category: {}", payload.getId());
            metadataService.removeCategory(payload);
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
