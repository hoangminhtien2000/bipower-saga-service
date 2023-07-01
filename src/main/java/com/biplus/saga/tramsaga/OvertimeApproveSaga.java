package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeApproveFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeApproveSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.SalaryServiceProxy;
import com.biplus.saga.tramsaga.state.salary.OvertimeApproveState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class OvertimeApproveSaga implements SimpleSaga<OvertimeApproveState> {
    private final SagaDefinition<OvertimeApproveState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public OvertimeApproveSaga(SalaryServiceProxy salaryServiceProxy,
                               EmailServiceProxy emailServiceProxy,
                               DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(salaryServiceProxy.overtimeApproveCommand, OvertimeApproveState::makeOvertimeApproveCommand)
                        .onReply(OvertimeApproveSuccessReply.class, OvertimeApproveState::onSuccess)
                        .onReply(OvertimeApproveFailureReply.class, OvertimeApproveState::onFailure)
                        .withCompensation(salaryServiceProxy.salaryRollbackCommand, OvertimeApproveState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand, OvertimeApproveState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, OvertimeApproveState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, OvertimeApproveState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, OvertimeApproveState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_APPROVE_OVERTIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_APPROVE_OVERTIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, OvertimeApproveState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_APPROVE_OVERTIME_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<OvertimeApproveState> getSagaDefinition() {
        return sagaDefinition;
    }

}
