package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveCreateFailureReply;
import com.biplus.saga.domain.message.leave.LeaveCreateSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.LeaveServiceProxy;
import com.biplus.saga.tramsaga.state.leave.LeaveCreateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;
@Component
public class LeaveCreateSaga implements SimpleSaga<LeaveCreateState> {
    private final SagaDefinition<LeaveCreateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public LeaveCreateSaga(LeaveServiceProxy leaveServiceProxy,
                           EmailServiceProxy emailServiceProxy,
                           DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //leave
                step()
                        .invokeParticipant(leaveServiceProxy.leaveCreateCommand, LeaveCreateState::makeLeaveCreateCommand)
                        .onReply(LeaveCreateSuccessReply.class, LeaveCreateState::onSuccess)
                        .onReply(LeaveCreateFailureReply.class, LeaveCreateState::onFailure)
                        .withCompensation(leaveServiceProxy.leaveRollbackCommand, LeaveCreateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, LeaveCreateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, LeaveCreateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, LeaveCreateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, LeaveCreateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CREATE_LEAVE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CREATE_LEAVE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, LeaveCreateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CREATE_LEAVE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<LeaveCreateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
