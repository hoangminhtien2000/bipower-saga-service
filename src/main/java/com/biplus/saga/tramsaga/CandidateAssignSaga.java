package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateAssignFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateAssignSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.CandidateAssignState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class CandidateAssignSaga implements SimpleSaga<CandidateAssignState> {

    private final SagaDefinition<CandidateAssignState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public CandidateAssignSaga(CandidateServiceProxy candidateServiceProxy,
                               EmailServiceProxy emailServiceProxy,
                               DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Candidate
                step()
                        .invokeParticipant(candidateServiceProxy.candidateAssignCommand, CandidateAssignState::makeCandidateAssignCommand)
                        .onReply(CandidateAssignSuccessReply.class, CandidateAssignState::onCandidateAssignSuccess)
                        .onReply(CandidateAssignFailureReply.class, CandidateAssignState::onCandidateAssignFailure)
                        .withCompensation(candidateServiceProxy.candidateAssignRollbackCommand, CandidateAssignState::makeCandidateAssignRollbackCommand)
                //Email
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, CandidateAssignState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, CandidateAssignState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, CandidateAssignState::onSendEmailFailure)

                .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, CandidateAssignState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.CANDIDATE_ASSIGN_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[CANDIDATE_ASSIGN_CHANNEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, CandidateAssignState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.CANDIDATE_ASSIGN_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<CandidateAssignState> getSagaDefinition() {
        return sagaDefinition;
    }
}
