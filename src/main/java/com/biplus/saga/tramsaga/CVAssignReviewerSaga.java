package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerFailureReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.CVAssignReviewerState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class CVAssignReviewerSaga implements SimpleSaga<CVAssignReviewerState> {

    private final SagaDefinition<CVAssignReviewerState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public CVAssignReviewerSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                CandidateServiceProxy candidateServiceProxy,
                                EmailServiceProxy emailServiceProxy,
                                DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.assignCVReviewerCommand, CVAssignReviewerState::makeAssignCVReviewerCommand)
                        .onReply(AssignCVReviewerSuccessReply.class, CVAssignReviewerState::onAssignCVReviewerSuccess)
                        .onReply(AssignCVReviewerFailureReply.class, CVAssignReviewerState::onAssignCVReviewerFailure)
                        .withCompensation(recruitmentServiceProxy.assignCVReviewerRollbackCommand, CVAssignReviewerState::makeAssignCVReviewerRollbackCommand)
                        //Candidate
                        .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, CVAssignReviewerState::makeCandidateStatusUpdatingCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, CVAssignReviewerState::onCandidateStatusUpdatingSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, CVAssignReviewerState::onCandidateStatusUpdatingFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, CVAssignReviewerState::makeCandidateStatusUpdatingRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, CVAssignReviewerState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, CVAssignReviewerState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, CVAssignReviewerState::onSendEmailFailure)
                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, CVAssignReviewerState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.ASSIGN_CV_REVIEWER_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[ASSIGN_CV_REVIEWER_CHANNEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, CVAssignReviewerState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.ASSIGN_CV_REVIEWER_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<CVAssignReviewerState> getSagaDefinition() {
        return sagaDefinition;
    }
}
