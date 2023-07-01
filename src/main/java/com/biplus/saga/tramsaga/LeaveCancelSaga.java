package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveCancelFailureReply;
import com.biplus.saga.domain.message.leave.LeaveCancelSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.LeaveServiceProxy;
import com.biplus.saga.tramsaga.state.leave.LeaveCancelState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class LeaveCancelSaga  implements SimpleSaga<LeaveCancelState> {
    private final SagaDefinition<LeaveCancelState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public LeaveCancelSaga(LeaveServiceProxy leaveServiceProxy,
                            EmailServiceProxy emailServiceProxy,
                            DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(leaveServiceProxy.leaveCancelCommand, LeaveCancelState::makeLeaveCancelCommand)
                        .onReply(LeaveCancelSuccessReply.class, LeaveCancelState::onSuccess)
                        .onReply(LeaveCancelFailureReply.class, LeaveCancelState::onFailure)
                        .withCompensation(leaveServiceProxy.leaveRollbackCommand, LeaveCancelState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand, LeaveCancelState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, LeaveCancelState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, LeaveCancelState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, LeaveCancelState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CANCEL_LEAVE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CANCEL_LEAVE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, LeaveCancelState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CANCEL_LEAVE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<LeaveCancelState> getSagaDefinition() {
        return sagaDefinition;
    }
}
