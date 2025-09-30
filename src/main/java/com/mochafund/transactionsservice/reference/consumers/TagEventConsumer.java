package com.mochafund.transactionsservice.reference.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.reference.consumers.payloads.TagEventPayload;
import com.mochafund.transactionsservice.common.util.CorrelationIdUtil;
import com.mochafund.transactionsservice.reference.service.WorkspaceMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagEventConsumer {

    private static final String GROUP_ID = "transactions-service";
    private final ObjectMapper objectMapper;
    private final WorkspaceMetadataService metadataService;

    @KafkaListener(topics = EventType.TAG_CREATED, groupId = GROUP_ID)
    public void handleTagCreated(String message) {
        EventEnvelope<TagEventPayload> event = this.readEnvelope(message, TagEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            TagEventPayload payload = event.getPayload();
            log.info("Processing tag.created - Tag: {}", payload.getId());
            metadataService.upsertTag(payload);
        });
    }

    @KafkaListener(topics = EventType.TAG_DELETED, groupId = GROUP_ID)
    public void handleTagDeleted(String message) {
        EventEnvelope<TagEventPayload> event = readEnvelope(message, TagEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            TagEventPayload payload = event.getPayload();
            log.info("Processing tag.deleted - Tag: {}", payload.getId());
            metadataService.removeTag(payload);
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
