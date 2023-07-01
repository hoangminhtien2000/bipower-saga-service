package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.AssignCVReviewerCommand;
import com.biplus.saga.domain.command.recruitment.AssignCVReviewerRollbackCommand;
import com.biplus.saga.domain.command.recruitment.SaveCVReviewCommand;
import com.biplus.saga.domain.command.recruitment.SaveCVReviewRollbackCommand;
import com.biplus.saga.domain.dto.candidate.AssignCVReviewCandidate;
import com.biplus.saga.domain.dto.candidate.CandidateContent;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerFailureReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveCVReviewFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveCVReviewSuccessReply;
import com.biplus.saga.domain.request.recruitment.AssignCVReviewerRequest;
import com.biplus.saga.domain.request.recruitment.SavingCvReviewRequest;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.EmployeeDataResponse;
import com.biplus.saga.domain.response.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingCVReviewState extends BaseSagaState {
    private final static String BUSINESS_CODE = "CV_REVIEW_RESULT";
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private Long candidateId;
    private String reviewStatus;
    private String note;

    private EmployeeDataResponse reviewer;
    private User inchargeUser;
    private CandidateResponse candidateResponse;
    private String candidateStatus;
    private Boolean isUpdated;

    public SavingCVReviewState(SavingCvReviewRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.candidateId = request.getCandidateId();
        this.reviewStatus = request.getReviewStatus();
        this.note = request.getNote();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("SavingCVReviewState SavingCvReviewRequest {}", JSonMapper.toJson(request));
        log.info("SavingCVReviewState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public SaveCVReviewCommand makeSavingCvReviewRequest() {
        log.info("makeSavingCvReviewRequest candidateId {},review status {}, review note {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(reviewStatus), JSonMapper.toJson(note));
        return SaveCVReviewCommand.builder()
                .candidateId(candidateId)
                .reviewStatus(reviewStatus)
                .note(note)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .build();
    }

    public void onSavingCvReviewSuccess(SaveCVReviewSuccessReply reply) {
        log.info("onSavingCvReviewSuccess {}", JSonMapper.toJson(reply));
        this.isUpdated = reply.getIsUpdated();
        this.candidateStatus = reply.getCandidateStatus();
        this.candidateResponse = reply.getCandidateResponse();
        this.reviewer = reply.getReviewer();
        this.inchargeUser = reply.getInchargeUser();
    }

    public void onSavingCvReviewFailure(SaveCVReviewFailureReply reply) {
        log.info("onSavingCvReviewFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public SaveCVReviewRollbackCommand makeSaveCVReviewRollbackCommand() {
        log.info("makeSaveCVReviewRollbackCommand candidateId {}, review status {}, review note {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(reviewStatus), JSonMapper.toJson(note));
        return SaveCVReviewRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
    }

    @RollbackOnException
    public CandidateStatusUpdatingCommand makeCandidateStatusUpdatingCommand() {
        log.info("makeCandidateStatusUpdatingCommand candidateId {}, candidateStatus {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(candidateStatus));
        return CandidateStatusUpdatingCommand.builder().candidateId(candidateId).candidateStatus(candidateStatus).isUpdated(isUpdated).sysDate(sysDate).locale(locale).companyCid(companyCid).actionUserDTO(actionUserDTO).build();
    }

    public void onCandidateStatusUpdatingSuccess(CandidateUpdateStatusSuccessReply reply) {
        log.info("onCandidateStatusUpdatingSuccess {}", JSonMapper.toJson(reply));
    }

    public void onCandidateStatusUpdatingFailure(CandidateUpdateStatusFailureReply reply) {
        log.info("onCandidateStatusUpdatingFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public CandidateStatusUpdatingRollbackCommand makeCandidateStatusUpdatingRollbackCommand() {
        log.info("makeCandidateStatusUpdatingRollbackCommand candidateId {}, status {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(candidateStatus));
        return CandidateStatusUpdatingRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("SavingCVReviewState makeSendEmailCommand");
        List<String> candidateNameLst = new ArrayList<>();
        candidateNameLst.add(reviewer.getFullName());
        candidateNameLst.add(candidateResponse.getFullName());

        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);
        CandidateContent candidateContent = CandidateContent.builder()
                .link(link)
                .name(candidateResponse.getFullName())
                .build();
        Map<String, Object> mapContent = new HashMap<>();
        mapContent.put("reviewer", candidateContent);
        String reviewResult = Boolean.parseBoolean(reviewStatus.trim()) ? MessageUtils.getMessage("review_cv_pass_message", locale) : MessageUtils.getMessage("review_cv_fail_message", locale);
        mapContent.put("result", reviewResult);
        mapContent.put("note", note);
        List<String> listEmail = new ArrayList<>();
        if (inchargeUser != null) {
            listEmail.add(inchargeUser.getEmail());
        }
        boolean isSendMail = true;
        if (CollectionUtils.isEmpty(listEmail)) {
            isSendMail = false;
        }
        String subjectCustom = MessageUtils.getMessage("email_title.review_cv_result", candidateNameLst);
        return SendEmailCommand.builder()
                .isSendMail(isSendMail)
                .businessCode(BUSINESS_CODE)
                .ccEmails(new ArrayList<>())
                .toEmails(listEmail)
                .paramsContent(mapContent)
                .paramsSubject(candidateNameLst)
                .sysDate(sysDate).locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .isCustomSubject(true)
                .subjectCustom(subjectCustom)
                .build();
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("onSendEmailSuccess {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }
}
