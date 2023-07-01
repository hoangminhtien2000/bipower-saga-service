package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.SendRequestWorkOutsideState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class SendRequestWorkOutsideSaga implements SimpleSaga<SendRequestWorkOutsideState> {
    private final SagaDefinition<SendRequestWorkOutsideState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SendRequestWorkOutsideSaga(AttendanceServiceProxy attendanceServiceProxy,
                                      EmailServiceProxy emailServiceProxy,
                                      DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.sendRequestWorkOutsideCommand,
                                SendRequestWorkOutsideState::makeSendRequestWorkOutsideCommand)
                        .onReply(SendRequestWorkOutsideSuccessReply.class, SendRequestWorkOutsideState::onSuccess)
                        .onReply(SendRequestWorkOutsideFailureReply.class, SendRequestWorkOutsideState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand,
                                SendRequestWorkOutsideState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand,
                                SendRequestWorkOutsideState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, SendRequestWorkOutsideState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, SendRequestWorkOutsideState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SendRequestWorkOutsideState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_SEND_REQUEST_WORK_OUTSIDE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_SEND_REQUEST_WORK_OUTSIDE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SendRequestWorkOutsideState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_SEND_REQUEST_WORK_OUTSIDE_CHANNEL,
                state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<SendRequestWorkOutsideState> getSagaDefinition() {
        return sagaDefinition;
    }
}
