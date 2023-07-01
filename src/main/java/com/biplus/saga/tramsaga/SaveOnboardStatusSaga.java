package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveOnboardStatusFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveOnboardStatusSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.EmployeeServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.CandidateAssignState;
import com.biplus.saga.tramsaga.state.employee.EmployeeCreateState;
import com.biplus.saga.tramsaga.state.recruitment.SaveOnboardStatusState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class SaveOnboardStatusSaga implements SimpleSaga<SaveOnboardStatusState> {

    private final SagaDefinition<SaveOnboardStatusState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SaveOnboardStatusSaga(
            RecruitmentServiceProxy recruitmentServiceProxy,
            CandidateServiceProxy candidateServiceProxy,
            EmailServiceProxy emailServiceProxy,
            EmployeeServiceProxy employeeServiceProxy,
            DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.saveOnboardStatusCommand, SaveOnboardStatusState::makeCommand)
                        .onReply(SaveOnboardStatusSuccessReply.class, SaveOnboardStatusState::onSaveSuccess)
                        .onReply(SaveOnboardStatusFailureReply.class, SaveOnboardStatusState::onSaveFailure)
                        .withCompensation(recruitmentServiceProxy.saveOnboardStatusRollbackCommand, SaveOnboardStatusState::makeRollbackCommand)
                        //Candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, SaveOnboardStatusState::makeUpdateCandidateStatusCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, SaveOnboardStatusState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, SaveOnboardStatusState::onChangeCandidateStatusFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, SaveOnboardStatusState::makeCandidateStatusUpdatingRollbackCommand)
                .step()
                        .invokeParticipant(employeeServiceProxy.employeeCreateCommand, SaveOnboardStatusState::makeEmployeeCreateCommand)
                        .onReply(EmployeeCreateSuccessReply.class, SaveOnboardStatusState::onEmployeeCreateSuccess)
                        .onReply(EmployeeCreateFailureReply.class, SaveOnboardStatusState::onEmployeeCreateFailure)
                        .withCompensation(employeeServiceProxy.employeeCreateRollbackCommand, SaveOnboardStatusState::makeEmployeeCreateRollbackCommand)
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, SaveOnboardStatusState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, SaveOnboardStatusState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, SaveOnboardStatusState::onSendEmailFailure)
                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SaveOnboardStatusState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SAVE_ONBOARD_STATUS);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SAVE_ONBOARD_STATUS] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SaveOnboardStatusState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.SAVE_ONBOARD_STATUS, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<SaveOnboardStatusState> getSagaDefinition() {
        return sagaDefinition;
    }
}
