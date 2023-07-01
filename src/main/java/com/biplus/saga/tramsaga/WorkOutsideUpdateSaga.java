package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideUpdateState;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideUpdateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class WorkOutsideUpdateSaga implements SimpleSaga<WorkOutsideUpdateState> {
    private final SagaDefinition<WorkOutsideUpdateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public WorkOutsideUpdateSaga(AttendanceServiceProxy attendanceServiceProxy,
                                 EmailServiceProxy emailServiceProxy,
                                 DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.workOutsideUpdateCommand, WorkOutsideUpdateState::makeWorkOutsideUpdateCommand)
                        .onReply(WorkOutsideUpdateSuccessReply.class, WorkOutsideUpdateState::onSuccess)
                        .onReply(WorkOutsideUpdateFailureReply.class, WorkOutsideUpdateState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand, WorkOutsideUpdateState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, WorkOutsideUpdateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, WorkOutsideUpdateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, WorkOutsideUpdateState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, WorkOutsideUpdateState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_UPDATE_WORK_OUTSIDE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_UPDATE_WORK_OUTSIDE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, WorkOutsideUpdateState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_UPDATE_WORK_OUTSIDE_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<WorkOutsideUpdateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
