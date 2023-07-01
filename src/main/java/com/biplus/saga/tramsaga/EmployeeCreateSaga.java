package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.EmployeeServiceProxy;
import com.biplus.saga.tramsaga.state.employee.EmployeeCreateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class EmployeeCreateSaga implements SimpleSaga<EmployeeCreateState> {

    private final SagaDefinition<EmployeeCreateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public EmployeeCreateSaga(EmployeeServiceProxy employeeServiceProxy,
                              EmailServiceProxy emailServiceProxy,
                              DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Employee
                step()
                        .invokeParticipant(employeeServiceProxy.employeeCreateCommand, EmployeeCreateState::makeEmployeeCreateCommand)
                        .onReply(EmployeeCreateSuccessReply.class, EmployeeCreateState::onEmployeeCreateSuccess)
                        .onReply(EmployeeCreateFailureReply.class, EmployeeCreateState::onEmployeeCreateFailure)
                        .withCompensation(employeeServiceProxy.employeeCreateRollbackCommand, EmployeeCreateState::makeEmployeeCreateRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, EmployeeCreateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, EmployeeCreateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, EmployeeCreateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, EmployeeCreateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.EMPLOYEE_CREATE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[CREATE_EMPLOYEE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, EmployeeCreateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.EMPLOYEE_CREATE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<EmployeeCreateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
