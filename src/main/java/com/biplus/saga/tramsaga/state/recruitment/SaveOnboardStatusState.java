package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.Constants;
import com.biplus.saga.common.DateUtil;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.employee.EmployeeCreateCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.recruitment.SaveOnboardStatusCommand;
import com.biplus.saga.domain.command.recruitment.SaveOnboardStatusRollbackCommand;
import com.biplus.saga.domain.dto.candidate.CandidateDTO;
import com.biplus.saga.domain.dto.candidate.CandidateFileDTO;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveOnboardStatusFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveOnboardStatusSuccessReply;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import com.biplus.saga.domain.request.recruitment.SaveOnboardStatusRequest;
import com.biplus.saga.domain.type.CandidateStatus;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveOnboardStatusState extends BaseSagaState {

    private String webRootPath;
    private String candidateDetailLink;

    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    private Long id;
    private Boolean candidateResponse;
    private LocalDateTime estimatedOnboardDate;
    private Boolean onboardStatus;
    private LocalDateTime onboardDate;
    private String note;
    private String rejectReason;
    private String rejectOnboardReason;

    private Long candidateId;
    private String candidateName;
    private String candidateLevel;
    private String candidatePosition;
    private String stackTech;
    private String emailTemplate;
    private List<String> toEmails;
    private CandidateDTO candidateDTO;
    private Long netSalary;

    public SaveOnboardStatusState(SaveOnboardStatusRequest request,
                                  String webRootPath,
                                  String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;

        this.id = request.getId();
        this.candidateResponse = request.getCandidateResponse();
        this.estimatedOnboardDate = DateUtil.parseLocalDateTime(
                request.getEstimatedOnboardDate(),
                "ESTIMATE_ONBOARD_DATE_INVALID"
        );
        this.onboardStatus = request.getOnboardStatus();
        this.onboardDate = DateUtil.parseLocalDateTime(
                request.getOnboardDate(),
                "ONBOARD_DATE_INVALID"
        );
        this.note = request.getNote();
        this.rejectReason = request.getRejectReason();
        this.rejectOnboardReason = request.getRejectOnboardReason();

        log.info("Init ApprovalDecisionState from request {}", request);
    }

    public SaveOnboardStatusCommand makeCommand() {
        SaveOnboardStatusCommand command = new SaveOnboardStatusCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make SaveOnboardStatusCommand {} to chanel {}: {}", command, ServiceChannel.RECRUITMENT_CHANNEL);
        return command;
    }

    public SaveOnboardStatusRollbackCommand makeRollbackCommand() {
        SaveOnboardStatusRollbackCommand command = new SaveOnboardStatusRollbackCommand();
        BeanUtils.copyProperties(this, command);
        log.info("Make SaveOnboardStatusRollbackCommand: {}", command);
        return command;
    }

    public void onSaveSuccess(SaveOnboardStatusSuccessReply reply) {
        log.info("Handle SaveOnboardStatusSuccessReply: {}", reply);
        this.candidateId = reply.getCandidateId();
        this.candidateName = reply.getCandidateName();
        this.candidateLevel = reply.getCandidateLevel();
        this.candidatePosition = reply.getCandidatePosition();
        this.stackTech = reply.getStackTech();
        this.emailTemplate = reply.getEmailTemplate();
        this.toEmails = reply.getToEmails();
        this.netSalary = reply.getNetSalary();
    }

    public void onSaveFailure(SaveOnboardStatusFailureReply reply) {
        log.info("Handle SaveOnboardStatusFailureReply: {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public CandidateStatusUpdatingCommand makeUpdateCandidateStatusCommand() {
        CandidateStatus status = CandidateStatus.R7;
        if (!this.candidateResponse) {
            status = CandidateStatus.R8_1;
        } else {
            status = CandidateStatus.R8;
        }
        if (this.onboardStatus != null) {
            status = this.onboardStatus ? CandidateStatus.R9 : CandidateStatus.R9_1;
        }


        CandidateStatusUpdatingCommand command = CandidateStatusUpdatingCommand.builder()
                .companyCid(this.companyCid)
                .sysDate(this.sysDate)
                .locale(this.locale)
                .actionUserDTO(this.actionUserDTO)
                .estimateOnboardDate(this.estimatedOnboardDate)
                .isUpdated(true)
                .candidateId(this.candidateId)
                .candidateStatus(status.value())
                .build();
        log.info("Make CandidateStatusUpdatingCommand: {}", command);
        return command;
    }


    public void onChangeCandidateStatusSuccess(CandidateUpdateStatusSuccessReply reply) {
        log.info("Handle CandidateUpdateStatusSuccessReply: {}", reply);
        this.candidateDTO = reply.getCandidateDTO();
    }

    public void onChangeCandidateStatusFailure(CandidateUpdateStatusFailureReply reply) {
        log.info("Handle CandidateUpdateStatusFailureReply: {}", reply);
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
    }

    public SendEmailCommand makeSendEmailCommand() {
        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);

        Map<String, Object> emailParams = new HashMap<>();
        String subject;
        String action;
        boolean isSendEmail = false;
        if (candidateResponse != null) {
            isSendEmail = true;
            this.emailTemplate = Constants.EmailTemplate.RESPONSE_OFFER;
        }
        if (onboardStatus != null) {
            isSendEmail = true;
            this.emailTemplate = Constants.EmailTemplate.ONBOARD_STATUS;
        }
        if (onboardDate == null) {
            isSendEmail = false;
        }
        if (emailTemplate.equals(Constants.EmailTemplate.RESPONSE_OFFER)) {
            action = MessageUtils.getMessage(candidateResponse ? "offer.accept" : "offer.reject");
            subject = MessageUtils.getMessage("email_title.response_offer",
                    candidateName, candidateLevel, candidatePosition,stackTech, action);
            emailParams.put("candidateLink", link);
            emailParams.put("candidateName", candidateName);
            emailParams.put("action", action);
            emailParams.put("estimateDate", DateUtil.format(estimatedOnboardDate));
            emailParams.put("rejectReason", rejectReason);
        } else {
            action = MessageUtils.getMessage(onboardStatus ? "onboard.join" : "onboard.missing");
            subject = MessageUtils.getMessage("email_title.onboard_status",
                    candidateName, candidateLevel, candidatePosition,stackTech, action);
            emailParams.put("candidateLink", link);
            emailParams.put("candidateName", candidateName);
            emailParams.put("action", action);
            emailParams.put("onboardDate", DateUtil.format(onboardDate));
            emailParams.put("reason", rejectOnboardReason);
        }
        emailParams.put("link", link);

        SendEmailCommand command = SendEmailCommand.builder()
                .isSendMail(isSendEmail)
                .businessCode(this.emailTemplate)
                .isCustomSubject(true)
                .subjectCustom(subject)
                .toEmails(toEmails)
                .ccEmails(new ArrayList<>())
                .paramsContent(emailParams)
                .build();
        log.info("Make SendEmailCommand: {}", command);
        return command;
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("Send email Onboard success: {}", reply);
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        setErrorCode(reply.getErrorCode());
        setErrorMessage(reply.getMessage());
        log.info("Send email Onboard failure: {}", reply);
    }

    public CandidateStatusUpdatingRollbackCommand makeCandidateStatusUpdatingRollbackCommand() {
        log.info("makeCandidateStatusUpdatingRollbackCommand");
        return CandidateStatusUpdatingRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    @RollbackOnException
    public EmployeeCreateCommand makeEmployeeCreateCommand() {
        log.info("makeEmployeeCreateCommand after candidate onboard");
        EmployeeCreateCommand employeeCreateCommand = new EmployeeCreateCommand();
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        if (candidateDTO != null) {
            if (StringUtils.hasText(candidateDTO.getFullName())) {
                if (!candidateDTO.getFullName().contains(" ")) {
                    request.setFirstName(candidateDTO.getFullName());
                } else {
                    int lastSpaceIndex = candidateDTO.getFullName().lastIndexOf(" ");
                    request.setFirstName(candidateDTO.getFullName().substring(lastSpaceIndex + 1));
                    request.setLastName(candidateDTO.getFullName().substring(0, lastSpaceIndex));
                }
            }
            request.setIndividualEmail(candidateDTO.getEmail());
            request.setPhone(candidateDTO.getPhone());
            request.setBirthday(candidateDTO.getBirthDate() == null ? null : candidateDTO.getBirthDate().toLocalDate());
            request.setGender(candidateDTO.getGender());
            request.setPosition(Collections.singletonList(candidatePosition));
            request.setStackTech(stackTech);
            request.setWorkingTimeFrom(candidateDTO.getStartWorkTime() == null ? null : candidateDTO.getStartWorkTime().toLocalDate());
            request.setWorkingTimeWithStackFrom(candidateDTO.getStartTechnologyTime() == null ? null : candidateDTO.getStartTechnologyTime().toLocalDate());
            if (!CollectionUtils.isEmpty(candidateDTO.getFiles())) {
                CandidateFileDTO candidateFileDTO = candidateDTO.getFiles().stream()
                        .max(Comparator.comparing(CandidateFileDTO::getLastModifiedAt)).orElse(null);
                request.setCvPath(candidateFileDTO == null || candidateFileDTO.getFile() == null ? null : candidateFileDTO.getFile().getFullPath());
            }
            request.setOnboardDate(onboardDate == null ? null : onboardDate.toLocalDate());
            request.setCurrentAddress(candidateDTO.getAddress());
            request.setCandidateId(candidateId);
            request.setNetSalary(netSalary);
        }
        employeeCreateCommand.setRequest(request);
        employeeCreateCommand.setActionUserDTO(actionUserDTO);
        employeeCreateCommand.setLocale(locale);
        employeeCreateCommand.setSysDate(sysDate);
        employeeCreateCommand.setCompanyCid(companyCid);
        return employeeCreateCommand;
    }

    public void onEmployeeCreateSuccess(EmployeeCreateSuccessReply reply) {
        log.info("onEmployeeCreateSuccess {}", JSonMapper.toJson(reply));

    }

    public void onEmployeeCreateFailure(EmployeeCreateFailureReply reply) {
        log.info("onEmployeeCreateFailure after candidate onboard: {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public EmployeeRollbackCommand makeEmployeeCreateRollbackCommand() {
        log.info("makeEmployeeCreateRollbackCommand create employee after candidate onboard");
        return EmployeeRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
