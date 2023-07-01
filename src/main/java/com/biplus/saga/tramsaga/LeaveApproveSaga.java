package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveApproveFailureReply;
import com.biplus.saga.domain.message.leave.LeaveApproveSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.LeaveServiceProxy;
import com.biplus.saga.tramsaga.state.leave.LeaveApproveState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class LeaveApproveSaga implements SimpleSaga<LeaveApproveState> {
    private final SagaDefinition<LeaveApproveState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public LeaveApproveSaga(LeaveServiceProxy leaveServiceProxy,
                            EmailServiceProxy emailServiceProxy,
                            DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(leaveServiceProxy.leaveApproveCommand, LeaveApproveState::makeLeaveApproveCommand)
                        .onReply(LeaveApproveSuccessReply.class, LeaveApproveState::onSuccess)
                        .onReply(LeaveApproveFailureReply.class, LeaveApproveState::onFailure)
                        .withCompensation(leaveServiceProxy.leaveRollbackCommand, LeaveApproveState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand, LeaveApproveState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, LeaveApproveState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, LeaveApproveState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, LeaveApproveState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_APPROVE_LEAVE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_APPROVE_LEAVE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, LeaveApproveState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_APPROVE_LEAVE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<LeaveApproveState> getSagaDefinition() {
        return sagaDefinition;
    }

}
