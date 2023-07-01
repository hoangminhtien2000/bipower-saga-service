package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.ConfirmManyRequestEditWorkingTimeFailureReply;
import com.biplus.saga.domain.message.attendance.ConfirmManyRequestEditWorkingTimeSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.ConfirmManyRequestEditWorkingTimeState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class ConfirmManyRequestEditWorkingTimeSaga implements SimpleSaga<ConfirmManyRequestEditWorkingTimeState> {
    private final SagaDefinition<ConfirmManyRequestEditWorkingTimeState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public ConfirmManyRequestEditWorkingTimeSaga(AttendanceServiceProxy attendanceServiceProxy,
                                          EmailServiceProxy emailServiceProxy,
                                          DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.confirmManyRequestEditWorkingTimeCommand,
                                ConfirmManyRequestEditWorkingTimeState::makeConfirmManyRequestEditWorkingTimeCommand)
                        .onReply(ConfirmManyRequestEditWorkingTimeSuccessReply.class, ConfirmManyRequestEditWorkingTimeState::onSuccess)
                        .onReply(ConfirmManyRequestEditWorkingTimeFailureReply.class, ConfirmManyRequestEditWorkingTimeState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand,
                                ConfirmManyRequestEditWorkingTimeState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand,
                                ConfirmManyRequestEditWorkingTimeState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, ConfirmManyRequestEditWorkingTimeState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, ConfirmManyRequestEditWorkingTimeState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, ConfirmManyRequestEditWorkingTimeState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CONFIRM_MAMY_REQUEST_EDIT_WORKING_TIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CONFIRM_MAMY_REQUEST_EDIT_WORKING_TIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, ConfirmManyRequestEditWorkingTimeState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CONFIRM_MAMY_REQUEST_EDIT_WORKING_TIME_CHANNEL,
                state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<ConfirmManyRequestEditWorkingTimeState> getSagaDefinition() {
        return sagaDefinition;
    }
}
