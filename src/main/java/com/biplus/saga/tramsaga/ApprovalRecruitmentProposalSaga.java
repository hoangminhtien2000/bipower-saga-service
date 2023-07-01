package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentProposalFailureReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentProposalSuccessReply;
import com.biplus.saga.proxy.CandidateServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.RecruitmentServiceProxy;
import com.biplus.saga.tramsaga.state.recruitment.ApprovalProposalState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Slf4j
@Component
public class ApprovalRecruitmentProposalSaga implements SimpleSaga<ApprovalProposalState> {

    private final SagaDefinition<ApprovalProposalState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public ApprovalRecruitmentProposalSaga(RecruitmentServiceProxy recruitmentServiceProxy,
                                           CandidateServiceProxy candidateServiceProxy,
                                           EmailServiceProxy emailServiceProxy,
                                           DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                // recruitment
                step()
                        .invokeParticipant(recruitmentServiceProxy.approvalRecruitmentProposalCommand, ApprovalProposalState::makeCommand)
                        .onReply(ApprovalRecruitmentProposalSuccessReply.class, ApprovalProposalState::onApprovalSuccess)
                        .onReply(ApprovalRecruitmentProposalFailureReply.class, ApprovalProposalState::onApprovalFailure)
                        .withCompensation(recruitmentServiceProxy.approvalRecruitmentProposalRollbackCommand, ApprovalProposalState::makeRollbackCommand)
                // candidate
                .step()
                        .invokeParticipant(candidateServiceProxy.candidateStatusUpdatingCommand, ApprovalProposalState::makeUpdateCandidateStatusCommand)
                        .onReply(CandidateUpdateStatusSuccessReply.class, ApprovalProposalState::onChangeCandidateStatusSuccess)
                        .onReply(CandidateUpdateStatusFailureReply.class, ApprovalProposalState::onChangeCandidateStatusFailure)
                        .withCompensation(candidateServiceProxy.candidateStatusUpdatingRollbackCommand, ApprovalProposalState::makeCandidateRollbackCommand)
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, ApprovalProposalState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, ApprovalProposalState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, ApprovalProposalState::onSendEmailFailure)
                        .build()
        );
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SagaDefinition<ApprovalProposalState> getSagaDefinition() {
        return this.sagaDefinition;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, ApprovalProposalState saveInterviewScheduleState) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.APPROVAL_RECRUITMENT_PROPOSAL_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[APPROVAL_RECRUITMENT_PROPOSAL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, ApprovalProposalState state) {
        log.error("Approval recruitment proposal rollback");
        domainEventPublisher.publish(
                COMPLETE_SAGA_CHANNEL,
                sagaId,
                singletonList(new SagaRollbackEvent(
                        ActionType.APPROVAL_RECRUITMENT_PROPOSAL_CHANNEL,
                        state.getErrorCode(), state.getErrorMessage())
                )
        );
    }
}
