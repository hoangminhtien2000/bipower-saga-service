package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeCreateFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeCreateSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.SalaryServiceProxy;
import com.biplus.saga.tramsaga.state.salary.OvertimeCreateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class OvertimeCreateSaga implements SimpleSaga<OvertimeCreateState> {
    private final SagaDefinition<OvertimeCreateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public OvertimeCreateSaga(SalaryServiceProxy salaryServiceProxy,
                              EmailServiceProxy emailServiceProxy,
                              DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(salaryServiceProxy.overtimeCreateCommand, OvertimeCreateState::makeOvertimeCreateCommand)
                        .onReply(OvertimeCreateSuccessReply.class, OvertimeCreateState::onSuccess)
                        .onReply(OvertimeCreateFailureReply.class, OvertimeCreateState::onFailure)
                        .withCompensation(salaryServiceProxy.salaryRollbackCommand, OvertimeCreateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, OvertimeCreateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, OvertimeCreateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, OvertimeCreateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, OvertimeCreateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CREATE_OVERTIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CREATE_OVERTIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, OvertimeCreateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CREATE_OVERTIME_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<OvertimeCreateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
