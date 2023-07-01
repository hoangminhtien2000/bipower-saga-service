package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.DeleteCVReviewerFailureReply;
import com.biplus.saga.domain.message.recruitment.DeleteCVReviewerSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.DeleteCVReviewerState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class DeleteCVReviewerSaga implements SimpleSaga<DeleteCVReviewerState> {
    private final SagaDefinition<DeleteCVReviewerState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public DeleteCVReviewerSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                CandidateServiceProxy candidateServiceProxy,
                                DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.deleteCVReviewerCommand, DeleteCVReviewerState::makeDeleteCVReviewerCommand)
                        .onReply(DeleteCVReviewerSuccessReply.class, DeleteCVReviewerState::onDeleteCVReviewerSuccess)
                        .onReply(DeleteCVReviewerFailureReply.class, DeleteCVReviewerState::onDeleteCVReviewerFailure)
                        .withCompensation(recruitmentServiceProxy.deleteCVReviewerRollbackCommand, DeleteCVReviewerState::makeDeleteCVReviewerRollbackCommand)
                        //Candidate
                        .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, DeleteCVReviewerState::makeCandidateStatusUpdatingCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, DeleteCVReviewerState::onCandidateStatusUpdatingSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, DeleteCVReviewerState::onCandidateStatusUpdatingFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, DeleteCVReviewerState::makeCandidateStatusUpdatingRollbackCommand)
                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, DeleteCVReviewerState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.DELETE_CV_REVIEWER_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[DELETE_CV_REVIEWER_CHANNEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, DeleteCVReviewerState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.DELETE_CV_REVIEWER_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<DeleteCVReviewerState> getSagaDefinition() {
        return sagaDefinition;
    }
}
