package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.ApprovalDecisionCommand;
import com.biplus.saga.domain.command.recruitment.ApprovalDecisionRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentDecisionFailureReply;
import com.biplus.saga.domain.message.recruitment.ApprovalRecruitmentDecisionSuccessReply;
import com.biplus.saga.domain.request.recruitment.ApprovalRecruitmentDecisionRequest;
import com.biplus.saga.domain.type.CandidateStatus;
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
public class ApprovalDecisionState extends BaseSagaState {

    private String businessCode = "RECRUITMENT_DECISION_APPROVAL";
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
    private String hrLeadName;
    private String candidateLevel;
    private String candidatePosition;
    private List<String> toEmails;

    public ApprovalDecisionState(ApprovalRecruitmentDecisionRequest request,
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
        log.info("Init ApprovalDecisionState from request {}", request);
    }

    public ApprovalDecisionCommand makeCommand() {
        ApprovalDecisionCommand command = new ApprovalDecisionCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make ApprovalDecisionCommand: {}", command);
        return command;
    }

    public ApprovalDecisionRollbackCommand makeRollbackCommand() {
        ApprovalDecisionRollbackCommand command = new ApprovalDecisionRollbackCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make ApprovalDecisionRollbackCommand: {}", command);
        return command;
    }

    public void onApprovalSuccess(ApprovalRecruitmentDecisionSuccessReply reply) {
        log.info("Handle ApprovalRecruitmentDecisionSuccessReply: {}", reply);
        this.candidateName = reply.getCandidateName();
        this.candidateLevel = reply.getCandidateLevel();
        this.candidatePosition = reply.getCandidatePosition();
        this.candidateId = reply.getCandidateId();
        this.toEmails = reply.getToEmails();
        this.hrLeadName = reply.getHrLeadName();
    }

    public void onApprovalFailure(ApprovalRecruitmentDecisionFailureReply reply) {
        log.info("Handle ApprovalRecruitmentDecisionFailureReply: {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public CandidateStatusUpdatingCommand makeUpdateCandidateStatusCommand() {
        CandidateStatusUpdatingCommand command = CandidateStatusUpdatingCommand.builder()
                .companyCid(this.companyCid)
                .sysDate(this.sysDate)
                .locale(this.locale)
                .actionUserDTO(this.actionUserDTO)
                .isUpdated(true)
                .candidateId(this.candidateId)
                .candidateStatus(approval ? CandidateStatus.R7.value() : CandidateStatus.R7_1.value())
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
        log.info("Handle CandidateUpdateStatusFailureReply: {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }


    public SendEmailCommand makeSendEmailCommand() {
        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);

        String approvalCode = approval ? "recruitment.approve" : "recruitment.reject";
        String approvalText = MessageUtils.getMessage(approvalCode);

        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("HrLead", hrLeadName);
        emailParams.put("approval", approvalText);
        emailParams.put("candidateLink", link);
        emailParams.put("candidateName", candidateName);
        emailParams.put("detailLink", link);
        emailParams.put("showNote", !approval);
        emailParams.put("note", note);


        SendEmailCommand command = SendEmailCommand.builder()
                .isSendMail(true)
                .businessCode(this.businessCode)
                .toEmails(toEmails)
                .isCustomSubject(true)
                .subjectCustom(MessageUtils.getMessage("email_title.approval_decision",
                        hrLeadName,
                        approvalText,
                        candidateName,
                        candidateLevel,
                        candidatePosition))
                .ccEmails(new ArrayList<>())
                .paramsContent(emailParams)
                .build();
        log.info("Make SendEmailCommand: {}", command);
        return command;
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("Send email approval decision success: {}", reply);
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("Send email approval decision failure: {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }

}
