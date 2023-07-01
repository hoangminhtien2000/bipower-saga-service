package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewScheduleFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewScheduleSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.SaveInterviewScheduleState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class SaveInterviewScheduleSaga implements SimpleSaga<SaveInterviewScheduleState> {

    private final SagaDefinition<SaveInterviewScheduleState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SaveInterviewScheduleSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                    CandidateServiceProxy candidateServiceProxy,
                                    EmailServiceProxy emailServiceProxy,
                                    DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                // recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.saveInterviewScheduleCommand, SaveInterviewScheduleState::makeCommand)
                        .onReply(SaveInterviewScheduleSuccessReply.class, SaveInterviewScheduleState::onSaveScheduleSuccess)
                        .onReply(SaveInterviewScheduleFailureReply.class, SaveInterviewScheduleState::onSaveScheduleFailure)
                        .withCompensation(recruitmentServiceProxy.saveInterviewScheduleRollbackCommand, SaveInterviewScheduleState::makeRollbackCommand)
                // candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, SaveInterviewScheduleState::makeUpdateCandidateStatusCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, SaveInterviewScheduleState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, SaveInterviewScheduleState::onChangeCandidateStatusFailure)
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, SaveInterviewScheduleState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, SaveInterviewScheduleState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, SaveInterviewScheduleState::onSendEmailFailure)
                        .build()
        );
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SagaDefinition<SaveInterviewScheduleState> getSagaDefinition() {
        return this.sagaDefinition;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SaveInterviewScheduleState saveInterviewScheduleState) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SAVE_INTERVIEW_SCHEDULE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SAVE_INTERVIEW_SCHEDULE_CHANEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SaveInterviewScheduleState state) {
        log.error("Save interview schedule rollback");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.SAVE_INTERVIEW_SCHEDULE_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }
}
