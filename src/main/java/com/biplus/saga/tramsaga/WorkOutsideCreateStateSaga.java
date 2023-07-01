package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.WorkOutsideCreateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideCreateSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideCreateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class WorkOutsideCreateStateSaga implements SimpleSaga<WorkOutsideCreateState> {
    private final SagaDefinition<WorkOutsideCreateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public WorkOutsideCreateStateSaga(AttendanceServiceProxy attendanceServiceProxy,
                                 EmailServiceProxy emailServiceProxy,
                                 DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.createWorkOutsideCommand, WorkOutsideCreateState::makeWorkOutsideCreateCommand)
                        .onReply(WorkOutsideCreateSuccessReply.class, WorkOutsideCreateState::onSuccess)
                        .onReply(WorkOutsideCreateFailureReply.class, WorkOutsideCreateState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand, WorkOutsideCreateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, WorkOutsideCreateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, WorkOutsideCreateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, WorkOutsideCreateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, WorkOutsideCreateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CREATE_WORK_OUTSIDE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CREATE_WORK_OUTSIDE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, WorkOutsideCreateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CREATE_WORK_OUTSIDE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<WorkOutsideCreateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
