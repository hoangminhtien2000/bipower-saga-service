package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.DateUtil;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.SaveInterviewScheduleCommand;
import com.biplus.saga.domain.command.recruitment.SaveInterviewScheduleRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewScheduleFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewScheduleSuccessReply;
import com.biplus.saga.domain.request.recruitment.SaveInterviewScheduleRequest;
import com.biplus.saga.domain.type.CandidateStatus;
import com.biplus.saga.tramsaga.ServiceChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveInterviewScheduleState extends BaseSagaState {

    private Long id;
    private Long candidateId;
    private String title;
    private LocalDateTime interviewFromTime;
    private LocalDateTime interviewToTime;
    private Long placeId;
    private Long interviewTypeId;
    private Long hrId;
    private Long interviewerId;
    private String interviewLink;
    private String description;
    private Long currentSalary;
    private Long expectSalary;
    private LocalDateTime onboardTime;
    private String note;
    private Boolean isJoined;
    private boolean updateCandidateStatus;

    private String businessCode = "SAVE_INTERVIEW_SCHEDULE";
    private String webRootPath;
    private String candidateName;
    private String interviewType;
    private String candidateDetailLink;
    private String candidateLevel;
    private String candidatePosition;
    private List<String> toEmails;

    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    public SaveInterviewScheduleState(SaveInterviewScheduleRequest request,
                                      String webRootPath,
                                      String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        BeanUtils.copyProperties(request, this, "interviewFromTime", "interviewToTime");
        this.interviewFromTime = DateUtil.parseLocalDateTime(request.getInterviewFromTime(), "INTERVIEW_TIME_INVALID");
        this.interviewToTime = DateUtil.parseLocalDateTime(request.getInterviewToTime(), "INTERVIEW_TIME_INVALID");
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
    }

    @RollbackOnException
    public SaveInterviewScheduleCommand makeCommand() {
        SaveInterviewScheduleCommand command = new SaveInterviewScheduleCommand();
        BeanUtils.copyProperties(this, command);
        log.info("make save interview schedule command to chanel {}: {}", ServiceChannel.RECRUITMENT_CHANNEL, command);
        return command;
    }

    public SaveInterviewScheduleRollbackCommand makeRollbackCommand() {
        SaveInterviewScheduleRollbackCommand rollbackCommand = new SaveInterviewScheduleRollbackCommand();
        BeanUtils.copyProperties(this, rollbackCommand);
        log.info("make save interview schedule rollback command");
        return rollbackCommand;
    }

    public CandidateStatusUpdatingCommand makeUpdateCandidateStatusCommand() {
        return CandidateStatusUpdatingCommand
                .builder()
                .locale(locale)
                .sysDate(sysDate)
                .actionUserDTO(actionUserDTO)
                .companyCid(companyCid)
                .candidateId(candidateId)
                .candidateStatus(Boolean.TRUE.equals(this.isJoined) ? CandidateStatus.R6.value() : CandidateStatus.R6_1.value())
                .isUpdated(true)
                .build();
    }

    public void onSaveScheduleSuccess(SaveInterviewScheduleSuccessReply reply) {
        log.info("Save interview schedule success {}", reply);
        this.updateCandidateStatus = reply.isUpdateCandidateStatus();
        this.candidateName = reply.getCandidateName();
        this.interviewType = reply.getInterviewType();
        this.candidateLevel = reply.getCandidateLevel();
        this.candidatePosition = reply.getCandidatePosition();
        this.toEmails = reply.getToEmails();
    }

    public void onSaveScheduleFailure(SaveInterviewScheduleFailureReply reply) {
        log.info("Save interview schedule failure {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }

    public void onChangeCandidateStatusSuccess(CandidateUpdateStatusSuccessReply reply) {
        log.info("Change candidate status success {}", reply);
    }

    public void onChangeCandidateStatusFailure(CandidateUpdateStatusFailureReply reply) {
        log.info("Change candidate status failure {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }

    public SendEmailCommand makeSendEmailCommand() {
        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);


        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("candidateLink", link);
        emailParams.put("candidateName", candidateName);
        emailParams.put("interviewDate", this.interviewFromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        emailParams.put("interviewTime", String.format("%s - %s",
                this.interviewFromTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                this.interviewToTime.format(DateTimeFormatter.ofPattern("HH:mm"))));
        emailParams.put("interviewType", interviewType);
        emailParams.put("interviewLink", StringUtils.trimToEmpty(interviewLink));


        log.info("make email command: {}", LocaleContextHolder.getLocale().getLanguage());
        String subject = MessageUtils.getMessage(
                "email_title.save_interview_schedule",
                candidateName,
                candidateLevel,
                candidatePosition,
                interviewFromTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        SendEmailCommand command = SendEmailCommand.builder()
                .isSendMail(true)
                .businessCode(this.businessCode)
                .toEmails(toEmails)
                .isCustomSubject(true)
                .subjectCustom(subject)
                .ccEmails(new ArrayList<>())
                .paramsContent(emailParams)
                .build();
        log.info("Make SendEmailCommand: {}", command);
        return command;
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("Send email for interview schedule success: {}", reply);
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("Send email for interview schedule failure: {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }

}
