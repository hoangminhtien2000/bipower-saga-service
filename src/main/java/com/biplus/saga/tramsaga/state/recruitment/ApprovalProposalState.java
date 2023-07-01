package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.ApprovalProposalCommand;
import com.biplus.saga.domain.command.recruitment.ApprovalProposalRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentProposalFailureReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentProposalSuccessReply;
import com.biplus.saga.domain.request.recruitment.ApprovalRecruitmentProposalRequest;
import com.biplus.saga.domain.type.CandidateStatus;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalProposalState extends BaseSagaState {

    private String businessCode = "RECRUITMENT_PROPOSAL";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    private Long id;
    private String note;
    private Boolean approval;
    private String webRootPath;
    private String candidateDetailLink;

    private Long candidateId;
    private String candidateName;
    private String inchargeHr;
    private List<String> toEmails;

    public ApprovalProposalState(ApprovalRecruitmentProposalRequest request,
                                 String webRootPath,
                                 String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();

        this.id = request.getId();
        this.note = request.getNote();
        this.approval = request.getApproval();

        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("Init ApprovalProposalState from request {}", request);
    }

    public ApprovalProposalCommand makeCommand() {
        ApprovalProposalCommand command = new ApprovalProposalCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make ApprovalProposalCommand: {}", command);
        return command;
    }

    public ApprovalProposalRollbackCommand makeRollbackCommand() {
        ApprovalProposalRollbackCommand command = new ApprovalProposalRollbackCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make ApprovalProposalRollbackCommand: {}", command);
        return command;
    }

    public void onApprovalSuccess(ApprovalRecruitmentProposalSuccessReply reply) {
        log.info("Handle ApprovalRecruitmentProposalSuccessReply: {}", reply);
        this.candidateId = reply.getCandidateId();
        this.candidateName = reply.getCandidateName();
        this.inchargeHr = reply.getInchargeHr();
        this.toEmails = reply.getToEmails();
    }

    public void onApprovalFailure(ApprovalRecruitmentProposalFailureReply reply) {
        log.info("Handle ApprovalRecruitmentProposalFailureReply: {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public CandidateStatusUpdatingCommand makeUpdateCandidateStatusCommand() {
        CandidateStatusUpdatingCommand command = CandidateStatusUpdatingCommand.builder()
                .companyCid(this.companyCid)
                .sysDate(this.sysDate)
                .locale(this.locale)
                .actionUserDTO(this.actionUserDTO)
                // .isUpdated(!this.approval)
                .isUpdated(false)
                .candidateId(this.candidateId)
                .candidateStatus(CandidateStatus.R7_1.value())
                .build();
        log.info("Make CandidateStatusUpdatingCommand: {}", command);
        return command;
    }

    public CandidateStatusUpdatingRollbackCommand makeCandidateRollbackCommand() {
        CandidateStatusUpdatingRollbackCommand command = CandidateStatusUpdatingRollbackCommand
                .builder()
                .companyCid(this.companyCid)
                .sysDate(this.sysDate)
                .locale(this.locale)
                .build();
        log.info("Make CandidateStatusUpdatingRollbackCommand: {}", command);
        return command;
    }

    public void onChangeCandidateStatusSuccess(CandidateUpdateStatusSuccessReply reply) {
        log.info("Handle CandidateUpdateStatusSuccessReply: {}", reply);
    }

    public void onChangeCandidateStatusFailure(CandidateUpdateStatusFailureReply reply) {
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
        log.info("Handle CandidateUpdateStatusFailureReply: {}", reply);
    }

    public SendEmailCommand makeSendEmailCommand() {
        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);

        Map<String, Object> emailParams = new HashMap<>();
        Map<String, Object> candidateParam = new HashMap<>();
        candidateParam.put("link", link);
        candidateParam.put("name", this.candidateName);
        emailParams.put("inchargeHr", this.inchargeHr);
        emailParams.put("candidate", candidateParam);

        SendEmailCommand command = SendEmailCommand.builder()
                .isSendMail(true)
                .businessCode(this.businessCode)
                .toEmails(toEmails)
                .ccEmails(new ArrayList<>())
                .paramsContent(emailParams)
                .build();
        log.info("Make SendEmailCommand: {}", command);
        return command;
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("Send email success: {}", reply);
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
        log.info("Send email failure: {}", reply);
    }

}
