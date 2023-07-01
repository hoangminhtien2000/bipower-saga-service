package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeUpdateFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeUpdateSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.SalaryServiceProxy;
import com.biplus.saga.tramsaga.state.salary.OvertimeUpdateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class OvertimeUpdateSaga implements SimpleSaga<OvertimeUpdateState> {
    private final SagaDefinition<OvertimeUpdateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public OvertimeUpdateSaga(SalaryServiceProxy salaryServiceProxy,
                              EmailServiceProxy emailServiceProxy,
                              DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(salaryServiceProxy.overtimeUpdateCommand, OvertimeUpdateState::makeOvertimeUpdateCommand)
                        .onReply(OvertimeUpdateSuccessReply.class, OvertimeUpdateState::onSuccess)
                        .onReply(OvertimeUpdateFailureReply.class, OvertimeUpdateState::onFailure)
                        .withCompensation(salaryServiceProxy.salaryRollbackCommand, OvertimeUpdateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, OvertimeUpdateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, OvertimeUpdateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, OvertimeUpdateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, OvertimeUpdateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_UPDATE_OVERTIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_UPDATE_OVERTIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, OvertimeUpdateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_UPDATE_OVERTIME_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<OvertimeUpdateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
