package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveCVReviewFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveCVReviewSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.SavingCVReviewState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class SavingCVReviewSaga implements SimpleSaga<SavingCVReviewState> {

    private final SagaDefinition<SavingCVReviewState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SavingCVReviewSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                              CandidateServiceProxy candidateServiceProxy,
                              EmailServiceProxy emailServiceProxy,
                              DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.saveCVReviewCommand, SavingCVReviewState::makeSavingCvReviewRequest)
                        .onReply(SaveCVReviewSuccessReply.class, SavingCVReviewState::onSavingCvReviewSuccess)
                        .onReply(SaveCVReviewFailureReply.class, SavingCVReviewState::onSavingCvReviewFailure)
                        .withCompensation(recruitmentServiceProxy.saveCVReviewRollbackCommand, SavingCVReviewState::makeSaveCVReviewRollbackCommand)
                        //Candidate
                        .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, SavingCVReviewState::makeCandidateStatusUpdatingCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, SavingCVReviewState::onCandidateStatusUpdatingSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, SavingCVReviewState::onCandidateStatusUpdatingFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, SavingCVReviewState::makeCandidateStatusUpdatingRollbackCommand)
                        //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, SavingCVReviewState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, SavingCVReviewState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, SavingCVReviewState::onSendEmailFailure)
                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SavingCVReviewState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SAVE_CV_REVIEW_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SAVE_CV_REVIEW_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SavingCVReviewState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.SAVE_CV_REVIEW_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<SavingCVReviewState> getSagaDefinition() {
        return sagaDefinition;
    }
}
