package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentDecisionFailureReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentDecisionSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.ApprovalDecisionState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class ApprovalRecruitmentDecisionSaga implements SimpleSaga<ApprovalDecisionState> {

    private final SagaDefinition<ApprovalDecisionState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public ApprovalRecruitmentDecisionSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                           CandidateServiceProxy candidateServiceProxy,
                                           EmailServiceProxy emailServiceProxy,
                                           DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                // recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.approvalRecruitmentDecisionCommand, ApprovalDecisionState::makeCommand)
                        .onReply(ApprovalRecruitmentDecisionSuccessReply.class, ApprovalDecisionState::onApprovalSuccess)
                        .onReply(ApprovalRecruitmentDecisionFailureReply.class, ApprovalDecisionState::onApprovalFailure)
                        .withCompensation(recruitmentServiceProxy.approvalRecruitmentDecisionRollbackCommand, ApprovalDecisionState::makeRollbackCommand)
                // candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, ApprovalDecisionState::makeUpdateCandidateStatusCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, ApprovalDecisionState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, ApprovalDecisionState::onChangeCandidateStatusFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, ApprovalDecisionState::makeCandidateRollbackCommand)
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, ApprovalDecisionState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, ApprovalDecisionState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, ApprovalDecisionState::onSendEmailFailure)
                        .build()
        );
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SagaDefinition<ApprovalDecisionState> getSagaDefinition() {
        return this.sagaDefinition;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, ApprovalDecisionState saveInterviewScheduleState) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.APPROVAL_RECRUITMENT_DECISION_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[APPROVAL_RECRUITMENT_DECISION] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, ApprovalDecisionState state) {
        log.error("Approval recruitment decision rollback");
        domainEventPublisher.publish(
                COMPLETE_SAGA_CHANNEL,
                sagaId,
                singletonList(new SagaRollbackEvent(
                        ActionType.APPROVAL_RECRUITMENT_DECISION_CHANNEL,
                        state.getErrorCode(), state.getErrorMessage())
                )
        );
    }
}
