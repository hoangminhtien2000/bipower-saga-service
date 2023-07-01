package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewContactFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewContactSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.SaveInterviewContactState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class SaveInterviewContactSaga implements SimpleSaga<SaveInterviewContactState> {

    private final SagaDefinition<SaveInterviewContactState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public SaveInterviewContactSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                    CandidateServiceProxy candidateServiceProxy,
                                    DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                step()
                        .invokeParticipant(recruitmentServiceProxy.saveInterviewContactCommand, SaveInterviewContactState::makeSaveInterviewContactCommand)
                        .onReply(SaveInterviewContactSuccessReply.class, SaveInterviewContactState::onSaveInterviewContactSuccess)
                        .onReply(SaveInterviewContactFailureReply.class, SaveInterviewContactState::onSaveInterviewContactFailure)
                        .withCompensation(recruitmentServiceProxy.saveInterviewContactRollbackCommand, SaveInterviewContactState::makeSaveInterviewContactRollbackCommand)
                        // candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, SaveInterviewContactState::makeCandidateUpdatingCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, SaveInterviewContactState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, SaveInterviewContactState::onChangeCandidateStatusFailure)
                        .build()
        );
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SagaDefinition<SaveInterviewContactState> getSagaDefinition() {
        return sagaDefinition;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, SaveInterviewContactState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.SAVE_INTERVIEW_CONTACT_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[SAVE_INTERVIEW_CONTACT_CHANEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, SaveInterviewContactState state) {
        log.error("Save interview contact rollback");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.SAVE_INTERVIEW_CONTACT_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }
}
