package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.WorkingTimeUpdateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkingTimeUpdateSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.WorkingTimeUpdateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class WorkingTimeUpdateSaga implements SimpleSaga<WorkingTimeUpdateState> {
    private final SagaDefinition<WorkingTimeUpdateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public WorkingTimeUpdateSaga(AttendanceServiceProxy attendanceServiceProxy,
                                 EmailServiceProxy emailServiceProxy,
                                 DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.workingTimeUpdateCommand, WorkingTimeUpdateState::makeWorkingTimeUpdateCommand)
                        .onReply(WorkingTimeUpdateSuccessReply.class, WorkingTimeUpdateState::onSuccess)
                        .onReply(WorkingTimeUpdateFailureReply.class, WorkingTimeUpdateState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand, WorkingTimeUpdateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, WorkingTimeUpdateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, WorkingTimeUpdateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, WorkingTimeUpdateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, WorkingTimeUpdateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_UPDATE_WORKING_TIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_UPDATE_WORKING_TIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, WorkingTimeUpdateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_UPDATE_WORKING_TIME_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<WorkingTimeUpdateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
