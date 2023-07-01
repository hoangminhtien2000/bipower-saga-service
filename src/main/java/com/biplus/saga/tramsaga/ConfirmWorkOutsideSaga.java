package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.AttendanceServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.attendance.ConfirmWorkOutsideState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class ConfirmWorkOutsideSaga implements SimpleSaga<ConfirmWorkOutsideState> {
    private final SagaDefinition<ConfirmWorkOutsideState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public ConfirmWorkOutsideSaga(AttendanceServiceProxy attendanceServiceProxy,
                                                 EmailServiceProxy emailServiceProxy,
                                                 DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Salary
                step()
                        .invokeParticipant(attendanceServiceProxy.confirmWorkOutsideCommand,
                                ConfirmWorkOutsideState::makeConfirmWorkOutsideCommand)
                        .onReply(ConfirmWorkOutsideSuccessReply.class, ConfirmWorkOutsideState::onSuccess)
                        .onReply(ConfirmWorkOutsideFailureReply.class, ConfirmWorkOutsideState::onFailure)
                        .withCompensation(attendanceServiceProxy.attendanceRollbackCommand,
                                ConfirmWorkOutsideState::makeSalaryRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendMultiEmailCommand,
                                ConfirmWorkOutsideState::makeSendMultiEmailCommand)
                        .onReply(SendEmailSuccessReply.class, ConfirmWorkOutsideState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, ConfirmWorkOutsideState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, ConfirmWorkOutsideState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SALARY_CONFIRM_MANY_REQUEST_WORK_OUTSIDE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SALARY_CONFIRM_MANY_REQUEST_WORK_OUTSIDE_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, ConfirmWorkOutsideState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.SALARY_CONFIRM_MANY_REQUEST_WORK_OUTSIDE_CHANNEL,
                state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<ConfirmWorkOutsideState> getSagaDefinition() {
        return sagaDefinition;
    }
}
