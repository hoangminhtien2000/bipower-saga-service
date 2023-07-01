package com.biplus.saga.domain.event;

import com.biplus.saga.domain.dto.ActionType;
import com.biplus.core.dto.ActionUserDTO;
import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SagaCompletedEvent implements DomainEvent {
    protected ActionType actionType;
    protected String sagaId;
    protected LocalDateTime sysDate;
    protected Locale locale;
    private ActionUserDTO actionUserDTO;

    private String successMessage;
    protected String message;
    protected String requestId;
    private boolean success = true;
}
