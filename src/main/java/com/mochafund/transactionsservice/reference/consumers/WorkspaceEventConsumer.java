package com.mochafund.transactionsservice.reference.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.reference.consumers.payloads.WorkspaceEventPayload;
import com.mochafund.transactionsservice.common.util.CorrelationIdUtil;
import com.mochafund.transactionsservice.reference.service.WorkspaceMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkspaceEventConsumer {

    private static final String GROUP_ID = "transactions-service";
    private final ObjectMapper objectMapper;
    private final WorkspaceMetadataService metadataService;

    @KafkaListener(topics = EventType.WORKSPACE_CREATED, groupId = GROUP_ID)
    public void handleWorkspaceCreated(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.created - Workspace: {}", payload.getWorkspaceId());
            metadataService.upsertWorkspace(payload);
        });
    }

    @KafkaListener(topics = EventType.WORKSPACE_UPDATED, groupId = GROUP_ID)
    public void handleWorkspaceUpdated(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.updated - Workspace: {}", payload.getWorkspaceId());
            metadataService.upsertWorkspace(payload);
        });
    }

    @KafkaListener(topics = EventType.WORKSPACE_DELETED, groupId = GROUP_ID)
    public void handleWorkspaceDeleted(String message) {
        EventEnvelope<WorkspaceEventPayload> event = readEnvelope(message, WorkspaceEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            WorkspaceEventPayload payload = event.getPayload();
            log.info("Processing workspace.deleted - Workspace: {}", payload.getWorkspaceId());
            metadataService.deleteWorkspace(payload.getWorkspaceId());
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
