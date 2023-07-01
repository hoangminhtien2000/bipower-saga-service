package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.AssignCVReviewerCommand;
import com.biplus.saga.domain.command.recruitment.AssignCVReviewerRollbackCommand;
import com.biplus.saga.domain.dto.candidate.AssignCVReviewCandidate;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerFailureReply;
import com.biplus.saga.domain.message.recruitment.AssignCVReviewerSuccessReply;
import com.biplus.saga.domain.request.recruitment.AssignCVReviewerRequest;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.EmployeeDataResponse;
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
public class CVAssignReviewerState extends BaseSagaState {
    private final static String BUSINESS_CODE = "ASSIGN_CV_REVIEWER";
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private Long candidateId;
    private List<Long> reviewers;
    private CandidateResponse candidateResponse;
    private List<EmployeeDataResponse> reviewerInfos;
    private String candidateStatus;
    private Boolean isUpdated;
    private String note;

    public CVAssignReviewerState(AssignCVReviewerRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.candidateId = request.getCandidateId();
        this.reviewers = request.getReviewers();
        this.note = request.getNote();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("CandidateAssignState AssignCandidateRequest {}", JSonMapper.toJson(request));
        log.info("CandidateAssignState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public AssignCVReviewerCommand makeAssignCVReviewerCommand() {
        log.info("makeAssignCVReviewerCommand candidateId {}, reviewers {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(reviewers));
        return AssignCVReviewerCommand.builder().candidateId(candidateId).reviewers(reviewers).note(note).sysDate(sysDate).locale(locale).companyCid(companyCid).actionUserDTO(actionUserDTO).build();
    }

    public void onAssignCVReviewerSuccess(AssignCVReviewerSuccessReply reply) {
        log.info("onAssignCVReviewerSuccess {}", JSonMapper.toJson(reply));
        this.isUpdated = reply.getIsUpdated();
        this.candidateStatus = reply.getCandidateStatus();
        this.candidateResponse = reply.getCandidateResponse();
        this.reviewerInfos = reply.getReviewerInfos();
    }

    public void onAssignCVReviewerFailure(AssignCVReviewerFailureReply reply) {
        log.info("onAssignCVReviewerFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public AssignCVReviewerRollbackCommand makeAssignCVReviewerRollbackCommand() {
        log.info("makeAssignCVReviewerRollbackCommand candidateId {}, reviewers {}", JSonMapper.toJson(candidateId), JSonMapper.toJson(reviewers));
        return AssignCVReviewerRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
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
        log.info("AssignCVReviewerState makeSendEmailCommand");
        List<String> candidateNameLst = new ArrayList<>();
        candidateNameLst.add(candidateResponse.getFullName());

        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);
        AssignCVReviewCandidate candidateContent = AssignCVReviewCandidate.builder()
                .link(link)
                .name(candidateResponse.getFullName())
                .applyPosition(candidateResponse.getApplyPosition() == null ? "" : candidateResponse.getApplyPosition().getName())
                .level(candidateResponse.getLevel() == null ? "" : candidateResponse.getLevel().getName())
                .technology(candidateResponse.getTechnology() == null ? "" : candidateResponse.getTechnology().getName()).build();
        Map<String, Object> mapContent = new HashMap<>();
        mapContent.put("candidate", candidateContent);
        mapContent.put("assigner", String.format("%s _ %s", actionUserDTO.getCompanyCode(), actionUserDTO.getFullName()));
        mapContent.put("note", note);
        List<String> listEmail = reviewerInfos.stream().filter(EmployeeDataResponse::isSendEmail).map(EmployeeDataResponse::getCompanyEmail).collect(Collectors.toList());
        boolean isSendMail = true;
        if (CollectionUtils.isEmpty(listEmail)){
            isSendMail = false;
        }
        return SendEmailCommand.builder()
                .isSendMail(isSendMail)
                .businessCode(BUSINESS_CODE)
                .ccEmails(new ArrayList<>())
                .toEmails(listEmail).paramsContent(mapContent).paramsSubject(candidateNameLst).sysDate(sysDate).locale(locale).companyCid(companyCid).actionUserDTO(actionUserDTO).build();
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
