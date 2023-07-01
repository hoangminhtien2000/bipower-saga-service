package com.biplus.saga.handler;

import com.biplus.core.enums.ErrorCode;
import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaEventHandler {
    private final SagaCompletableTable sagaCompletableTable;

    private static final String SAGA_PROCESSING_MESSAGE = "This Saga instance does not process sagaId=";

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType(ServiceChannel.COMPLETE_SAGA_CHANNEL)
                .onEvent(SagaCompletedEvent.class, dee -> sagaCompletedEvent(dee.getEvent()))
                .onEvent(SagaRollbackEvent.class, this::rollbackSubscriber)
                .build();
    }

    private void sagaCompletedEvent(SagaCompletedEvent event) {
        CompletableFuture<SagaCompletedEvent> completableFuture = sagaCompletableTable.get(event.getActionType(), event.getSagaId());
        if (isNull(completableFuture)) {
            log.info(SAGA_PROCESSING_MESSAGE + event.getSagaId());
            return;
        }
        event.setSuccess(true);
        completableFuture.complete(event);
        sagaCompletableTable.remove(event.getActionType(), event.getSagaId());
    }

    private void rollbackSubscriber(DomainEventEnvelope<SagaRollbackEvent> dee) {
        SagaRollbackEvent event = Objects.requireNonNull(dee.getEvent());
        CompletableFuture completableFuture = sagaCompletableTable.get(event.getActionType(), dee.getAggregateId());
        if (isNull(completableFuture)) {
            log.info(SAGA_PROCESSING_MESSAGE + dee.getAggregateId());
            return;
        }
        if (ErrorCode.ILLEGAL_ARGUMENT == event.getErrorCode()) {
            completableFuture.completeExceptionally(new IllegalArgumentException(event.getMessage()));
        } else {
            completableFuture.completeExceptionally(new InternalServerErrorException(event.getMessage()));
        }
        sagaCompletableTable.remove(event.getActionType(), dee.getAggregateId());
    }

}
