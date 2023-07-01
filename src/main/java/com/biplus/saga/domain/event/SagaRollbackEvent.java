package com.biplus.saga.domain.event;

import com.biplus.saga.domain.dto.ActionType;
import com.biplus.core.enums.ErrorCode;
import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SagaRollbackEvent implements DomainEvent {
    private ActionType actionType;
    protected ErrorCode errorCode;
    protected String message = "System error";
    private boolean success;

    public SagaRollbackEvent(ActionType actionType, ErrorCode errorCode, String message) {
        this.actionType = actionType;
        this.errorCode = errorCode;
        this.message = message;
    }
}
