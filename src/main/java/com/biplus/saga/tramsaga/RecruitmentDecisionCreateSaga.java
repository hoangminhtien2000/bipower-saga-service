package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentDecisionFailureReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentDecisionSuccessReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentProposalFailureReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentProposalSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.DecisionCreateState;
import com.biplus.saga.tramsaga.state.recruitment.RecruitmentProposalCreateState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class RecruitmentDecisionCreateSaga implements SimpleSaga<DecisionCreateState> {
    private final SagaDefinition<DecisionCreateState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public RecruitmentDecisionCreateSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                         EmailServiceProxy emailServiceProxy,
                                         DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.createRecruitmentDecisionCommand, DecisionCreateState::makeCreateRecruitmentDecisionCommand)
                        .onReply(CreateRecruitmentDecisionSuccessReply.class, DecisionCreateState::onCreateRecruitmentDecisionSuccess)
                        .onReply(CreateRecruitmentDecisionFailureReply.class, DecisionCreateState::onCreateRecruitmentDecisionFailure)
                        .withCompensation(recruitmentServiceProxy.createRecruitmentDecisionRollbackCommand, DecisionCreateState::makeCreateRecruitmentDecisionRollbackCommand)
                       //Email
                        .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, DecisionCreateState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, DecisionCreateState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, DecisionCreateState::onSendEmailFailure)
                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, DecisionCreateState recruitmentDecisionCreateState) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.RECRUITMENT_DECISION_CREATE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[RECRUITMENT_DECISION_CREATE_CHANNEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }
    @Override
    public void onSagaRolledBack(String sagaId, DecisionCreateState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.RECRUITMENT_DECISION_CREATE_CHANNEL, state.getErrorCode(), state.getErrorMessage())));

    }

    @Override
    public SagaDefinition<DecisionCreateState> getSagaDefinition() {
        return sagaDefinition;
    }
}
