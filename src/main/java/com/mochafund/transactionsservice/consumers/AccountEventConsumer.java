package com.mochafund.transactionsservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mochafund.transactionsservice.common.events.EventEnvelope;
import com.mochafund.transactionsservice.common.events.EventType;
import com.mochafund.transactionsservice.common.events.payloads.AccountEventPayload;
import com.mochafund.transactionsservice.common.util.CorrelationIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountEventConsumer {

    private final String GROUP_ID = "transactions-service";
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = EventType.ACCOUNT_CREATED, groupId = GROUP_ID)
    public void handleAccountCreated(String message) {
        EventEnvelope<AccountEventPayload> event = readEnvelope(message, AccountEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            AccountEventPayload payload = event.getPayload();
            log.info("Processing account.created - Account: {}", payload.getId());
        });
    }

    @KafkaListener(topics = EventType.ACCOUNT_DELETED, groupId = GROUP_ID)
    public void handleAccountDeleted(String message) {
        EventEnvelope<AccountEventPayload> event = readEnvelope(message, AccountEventPayload.class);
        CorrelationIdUtil.executeWithCorrelationId(event, () -> {
            AccountEventPayload payload = event.getPayload();
            log.info("Processing account.deleted - Account: {}", payload.getId());
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
