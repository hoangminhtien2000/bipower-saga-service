package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveConfirmFailureReply;
import com.biplus.saga.domain.message.leave.LeaveConfirmSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.LeaveServiceProxy;
import com.biplus.saga.tramsaga.state.leave.LeaveConfirmState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class LeaveConfirmSaga  implements SimpleSaga<LeaveConfirmState> {
    private final SagaDefinition<LeaveConfirmState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public LeaveConfirmSaga(LeaveServiceProxy leaveServiceProxy,
                           EmailServiceProxy emailServiceProxy,
                           DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(leaveServiceProxy.leaveConfirmCommand, LeaveConfirmState::makeLeaveConfirmCommand)
                        .onReply(LeaveConfirmSuccessReply.class, LeaveConfirmState::onSuccess)
                        .onReply(LeaveConfirmFailureReply.class, LeaveConfirmState::onFailure)
                        .withCompensation(leaveServiceProxy.leaveRollbackCommand, LeaveConfirmState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand, LeaveConfirmState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, LeaveConfirmState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, LeaveConfirmState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, LeaveConfirmState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CONFIRM_LEAVE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CONFIRM_LEAVE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, LeaveConfirmState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CONFIRM_LEAVE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<LeaveConfirmState> getSagaDefinition() {
        return sagaDefinition;
    }
}
