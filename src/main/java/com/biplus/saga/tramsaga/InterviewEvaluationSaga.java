package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.InterviewEvaluationFailureReply;
import com.biplus.saga.domain.message.recruitment.InterviewEvaluationSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.InterviewEvaluationState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class InterviewEvaluationSaga implements SimpleSaga<InterviewEvaluationState> {

    private final SagaDefinition<InterviewEvaluationState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public InterviewEvaluationSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                   CandidateServiceProxy candidateServiceProxy,
                                   DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                // recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.interviewEvaluationCommand, InterviewEvaluationState::makeCommand)
                        .onReply(InterviewEvaluationSuccessReply.class, InterviewEvaluationState::onInterviewEvaluationSuccess)
                        .onReply(InterviewEvaluationFailureReply.class, InterviewEvaluationState::onInterviewEvaluationFailure)
                        .withCompensation(recruitmentServiceProxy.interviewEvaluationRollbackCommand, InterviewEvaluationState::makeRollbackCommand)
                // candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, InterviewEvaluationState::makeUpdateCandidateStatusCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, InterviewEvaluationState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, InterviewEvaluationState::onChangeCandidateStatusFailure)
                        .build()
        );
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SagaDefinition<InterviewEvaluationState> getSagaDefinition() {
        return this.sagaDefinition;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, InterviewEvaluationState saveInterviewScheduleState) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.INTERVIEW_EVALUATION_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[INTERVIEW_EVALUATION_CHANEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, InterviewEvaluationState state) {
        log.error("Interview evaluation rollback");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.INTERVIEW_EVALUATION_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }
}
