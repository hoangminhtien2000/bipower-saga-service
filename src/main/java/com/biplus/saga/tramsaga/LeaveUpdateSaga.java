package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveUpdateFailureReply;
import com.biplus.saga.domain.message.leave.LeaveUpdateSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.LeaveServiceProxy;
import com.biplus.saga.tramsaga.state.leave.LeaveUpdateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LeaveUpdateSaga implements SimpleSaga<LeaveUpdateState> {
    private final SagaDefinition<LeaveUpdateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public LeaveUpdateSaga(LeaveServiceProxy leaveServiceProxy,
                           EmailServiceProxy emailServiceProxy,
                           DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //leave
                step()
                        .invokeParticipant(leaveServiceProxy.leaveUpdateCommand, LeaveUpdateState::makeLeaveUpdateCommand)
                        .onReply(LeaveUpdateSuccessReply.class, LeaveUpdateState::onSuccess)
                        .onReply(LeaveUpdateFailureReply.class, LeaveUpdateState::onFailure)
                        .withCompensation(leaveServiceProxy.leaveRollbackCommand, LeaveUpdateState::makeSalaryRollbackCommand)
                        //Email
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, LeaveUpdateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, LeaveUpdateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, LeaveUpdateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, LeaveUpdateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_UPDATE_LEAVE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_UPDATE_LEAVE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(ServiceChannel.COMPLETE_SAGA_CHANNEL, sagaId, Collections.singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, LeaveUpdateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_UPDATE_LEAVE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(ServiceChannel.COMPLETE_SAGA_CHANNEL, sagaId,
                Collections.singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<LeaveUpdateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
