package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeConfirmFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeConfirmSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.SalaryServiceProxy;
import com.biplus.saga.tramsaga.state.salary.OvertimeConfirmState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class OvertimeConfirmSaga  implements SimpleSaga<OvertimeConfirmState> {
    private final SagaDefinition<OvertimeConfirmState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public OvertimeConfirmSaga(SalaryServiceProxy salaryServiceProxy,
                               EmailServiceProxy emailServiceProxy,
                               DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(salaryServiceProxy.overtimeConfirmCommand, OvertimeConfirmState::makeOvertimeConfirmCommand)
                        .onReply(OvertimeConfirmSuccessReply.class, OvertimeConfirmState::onSuccess)
                        .onReply(OvertimeConfirmFailureReply.class, OvertimeConfirmState::onFailure)
                        .withCompensation(salaryServiceProxy.salaryRollbackCommand, OvertimeConfirmState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand, OvertimeConfirmState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, OvertimeConfirmState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, OvertimeConfirmState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, OvertimeConfirmState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CONFIRM_OVERTIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CONFIRM_OVERTIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, OvertimeConfirmState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CONFIRM_OVERTIME_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<OvertimeConfirmState> getSagaDefinition() {
        return sagaDefinition;
    }

}
