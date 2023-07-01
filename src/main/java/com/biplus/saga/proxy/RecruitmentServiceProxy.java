package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.recruitment.*;
import com.biplus.saga.domain.message.recruitment.*;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentServiceProxy {
    public final CommandEndpoint<AssignCVReviewerCommand> assignCVReviewerCommand = CommandEndpointBuilder
            .forCommand(AssignCVReviewerCommand.class)
            .withReply(AssignCVReviewerSuccessReply.class)
            .withReply(AssignCVReviewerFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<DeleteCVReviewerCommand> deleteCVReviewerCommand = CommandEndpointBuilder
            .forCommand(DeleteCVReviewerCommand.class)
            .withReply(DeleteCVReviewerSuccessReply.class)
            .withReply(DeleteCVReviewerFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<AssignCVReviewerRollbackCommand> assignCVReviewerRollbackCommand = CommandEndpointBuilder
            .forCommand(AssignCVReviewerRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<DeleteCVReviewerRollbackCommand> deleteCVReviewerRollbackCommand = CommandEndpointBuilder
            .forCommand(DeleteCVReviewerRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();
    public final CommandEndpoint<SaveCVReviewCommand> saveCVReviewCommand = CommandEndpointBuilder
            .forCommand(SaveCVReviewCommand.class)
            .withReply(SaveCVReviewSuccessReply.class)
            .withReply(SaveCVReviewFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveCVReviewRollbackCommand> saveCVReviewRollbackCommand = CommandEndpointBuilder
            .forCommand(SaveCVReviewRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();


    public final CommandEndpoint<SaveInterviewContactCommand> saveInterviewContactCommand = CommandEndpointBuilder
            .forCommand(SaveInterviewContactCommand.class)
            .withReply(SaveInterviewContactSuccessReply.class)
            .withReply(SaveInterviewContactFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveInterviewContactRollbackCommand> saveInterviewContactRollbackCommand = CommandEndpointBuilder
            .forCommand(SaveInterviewContactRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveInterviewScheduleCommand> saveInterviewScheduleCommand = CommandEndpointBuilder
            .forCommand(SaveInterviewScheduleCommand.class)
            .withReply(SaveInterviewScheduleSuccessReply.class)
            .withReply(SaveInterviewScheduleFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveInterviewScheduleRollbackCommand> saveInterviewScheduleRollbackCommand = CommandEndpointBuilder
            .forCommand(SaveInterviewScheduleRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();


    public final CommandEndpoint<InterviewEvaluationCommand> interviewEvaluationCommand = CommandEndpointBuilder
            .forCommand(InterviewEvaluationCommand.class)
            .withReply(InterviewEvaluationSuccessReply.class)
            .withReply(InterviewEvaluationFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<InterviewEvaluationRollbackCommand> interviewEvaluationRollbackCommand = CommandEndpointBuilder
            .forCommand(InterviewEvaluationRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<CreateRecruitmentProposalCommand> createRecruitmentProposalCommand = CommandEndpointBuilder
            .forCommand(CreateRecruitmentProposalCommand.class)
            .withReply(CreateRecruitmentProposalSuccessReply.class)
            .withReply(CreateRecruitmentProposalFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<CreateRecruitmentProposalRollbackCommand> createRecruitmentProposalRollbackCommand = CommandEndpointBuilder
            .forCommand(CreateRecruitmentProposalRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<ApprovalProposalCommand> approvalRecruitmentProposalCommand = CommandEndpointBuilder
            .forCommand(ApprovalProposalCommand.class)
            .withReply(ApprovalRecruitmentProposalSuccessReply.class)
            .withReply(ApprovalRecruitmentProposalFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<ApprovalProposalRollbackCommand> approvalRecruitmentProposalRollbackCommand = CommandEndpointBuilder
            .forCommand(ApprovalProposalRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<RecruitmentDecisionCreateCommand> createRecruitmentDecisionCommand = CommandEndpointBuilder
            .forCommand(RecruitmentDecisionCreateCommand.class)
            .withReply(CreateRecruitmentDecisionSuccessReply.class)
            .withReply(CreateRecruitmentDecisionFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<RecruitmentDecisionCreateRollbackCommand> createRecruitmentDecisionRollbackCommand = CommandEndpointBuilder
            .forCommand(RecruitmentDecisionCreateRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();


    public final CommandEndpoint<ApprovalDecisionCommand> approvalRecruitmentDecisionCommand = CommandEndpointBuilder
            .forCommand(ApprovalDecisionCommand.class)
            .withReply(ApprovalRecruitmentDecisionSuccessReply.class)
            .withReply(ApprovalRecruitmentDecisionFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<ApprovalDecisionRollbackCommand> approvalRecruitmentDecisionRollbackCommand = CommandEndpointBuilder
            .forCommand(ApprovalDecisionRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveOnboardStatusCommand> saveOnboardStatusCommand = CommandEndpointBuilder
            .forCommand(SaveOnboardStatusCommand.class)
            .withReply(SaveOnboardStatusSuccessReply.class)
            .withReply(SaveOnboardStatusFailureReply.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

    public final CommandEndpoint<SaveOnboardStatusRollbackCommand> saveOnboardStatusRollbackCommand = CommandEndpointBuilder
            .forCommand(SaveOnboardStatusRollbackCommand.class)
            .withChannel(ServiceChannel.RECRUITMENT_CHANNEL)
            .build();

}
