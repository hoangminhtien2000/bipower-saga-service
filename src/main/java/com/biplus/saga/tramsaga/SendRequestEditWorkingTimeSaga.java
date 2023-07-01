package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.SendRequestEditWorkingTimeFailureReply;
import com.biplus.saga.domain.message.attendance.SendRequestEditWorkingTimeSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.SendRequestEditWorkingTimeState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class SendRequestEditWorkingTimeSaga implements SimpleSaga<SendRequestEditWorkingTimeState> {
    private final SagaDefinition<SendRequestEditWorkingTimeState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SendRequestEditWorkingTimeSaga(AttendanceServiceProxy attendanceServiceProxy,
                                          EmailServiceProxy emailServiceProxy,
                                          DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.sendRequestEditWorkingTimeCommand,
                                SendRequestEditWorkingTimeState::makeSendRequestEditWorkingTimeCommand)
                        .onReply(SendRequestEditWorkingTimeSuccessReply.class, SendRequestEditWorkingTimeState::onSuccess)
                        .onReply(SendRequestEditWorkingTimeFailureReply.class, SendRequestEditWorkingTimeState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand,
                                SendRequestEditWorkingTimeState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand,
                                SendRequestEditWorkingTimeState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, SendRequestEditWorkingTimeState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, SendRequestEditWorkingTimeState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SendRequestEditWorkingTimeState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_SEND_REQUEST_EDIT_WORKING_TIME_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_SEND_REQUEST_EDIT_WORKING_TIME_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SendRequestEditWorkingTimeState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_SEND_REQUEST_EDIT_WORKING_TIME_CHANNEL,
                state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<SendRequestEditWorkingTimeState> getSagaDefinition() {
        return sagaDefinition;
    }
}
