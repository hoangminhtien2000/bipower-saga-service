package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.RecruitmentDecisionCreateCommand;
import com.biplus.saga.domain.command.recruitment.RecruitmentDecisionCreateRollbackCommand;
import com.biplus.saga.domain.dto.candidate.CandidateContent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentDecisionFailureReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentDecisionSuccessReply;
import com.biplus.saga.domain.request.recruitment.CreatingRecruitmentDecisionRequest;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.RecruitmentDecisionResponse;
import com.biplus.saga.domain.response.User;
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
public class DecisionCreateState extends BaseSagaState {
    private final static String BUSINESS_CODE = "RECRUITMENT_DECISION_CREATE";
    private Long id;
    private Long candidateId;
    private Long contractTypeId;
    private Long contractPeriodId;
    private LocalDateTime startWorkDate;
    private String officialSalary;
    private String probationarySalary;
    private String basicSalary;
    private String negotiableSalary;
    private String proposedSalary;
    private Long workingPlaceId;
    private Long workingTimeId;
    private String compensationBenefit;
    private String effortReview;
    private String otherIncome;
    private String training;
    private String otherBenefit;
    private String jobDescription;
    private Long contactUserId;
    private Boolean status;
    private String inchargeHrNote;
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    private CandidateResponse candidateResponse;
    private RecruitmentDecisionResponse decision;
    private List<User> hrLeaders;

    public DecisionCreateState(CreatingRecruitmentDecisionRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.id = request.getId();
        this.candidateId = request.getCandidateId();
        this.contractTypeId = request.getContractTypeId();
        this.contractPeriodId = request.getContractPeriodId();
        this.startWorkDate = request.getStartWorkDate();
        this.officialSalary = request.getOfficialSalary();
        this.probationarySalary = request.getProbationarySalary();
        this.basicSalary = request.getBasicSalary();
        this.negotiableSalary = request.getNegotiableSalary();
        this.proposedSalary = request.getProposedSalary();
        this.workingPlaceId = request.getWorkingPlaceId();
        this.workingTimeId = request.getWorkingTimeId();
        this.compensationBenefit = request.getCompensationBenefit();
        this.effortReview = request.getEffortReview();
        this.otherIncome = request.getOtherIncome();
        this.training = request.getTraining();
        this.otherBenefit = request.getOtherBenefit();
        this.jobDescription = request.getJobDescription();
        this.contactUserId = request.getContactUserId();
        this.inchargeHrNote = request.getInchargeHrNote();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("RecruitmentDecisionCreateState CreatingRecruitmentDecisionRequest {}", JSonMapper.toJson(request));
        log.info("RecruitmentDecisionCreateState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public RecruitmentDecisionCreateCommand makeCreateRecruitmentDecisionCommand() {
        log.info("makeCreateRecruitmentDecisionCommand id {}, candidateId {}, contractTypeId {}, contractPeriodId {}, startWorkDate {}, officialSalary {}, probationarySalary {}, " +
                        "basicSalary {}, negotiableSalary {}, proposedSalary {}, workingPlaceId {}, workingTimeId {}, compensationBenefit {}, effortReview {}, otherIncome {}, training {}" +
                        ", otherBenefit {}, jobDescription {}, contactUserId {}, inchargeHrNote {}",
                JSonMapper.toJson(id), JSonMapper.toJson(candidateId), JSonMapper.toJson(contractTypeId), JSonMapper.toJson(contractPeriodId),
                JSonMapper.toJson(startWorkDate), JSonMapper.toJson(officialSalary), JSonMapper.toJson(probationarySalary), JSonMapper.toJson(basicSalary),
                JSonMapper.toJson(negotiableSalary), JSonMapper.toJson(proposedSalary), JSonMapper.toJson(workingPlaceId),
                JSonMapper.toJson(workingTimeId), JSonMapper.toJson(compensationBenefit), JSonMapper.toJson(effortReview),
                JSonMapper.toJson(otherIncome), JSonMapper.toJson(training), JSonMapper.toJson(otherBenefit),
                JSonMapper.toJson(jobDescription), JSonMapper.toJson(contactUserId), JSonMapper.toJson(inchargeHrNote));
        return RecruitmentDecisionCreateCommand.builder()
                .id(id)
                .candidateId(candidateId)
                .contractTypeId(contractTypeId)
                .contractPeriodId(contractPeriodId)
                .startWorkDate(startWorkDate)
                .officialSalary(officialSalary)
                .probationarySalary(probationarySalary)
                .basicSalary(basicSalary)
                .negotiableSalary(negotiableSalary)
                .proposedSalary(proposedSalary)
                .workingPlaceId(workingPlaceId)
                .workingTimeId(workingTimeId)
                .compensationBenefit(compensationBenefit)
                .effortReview(effortReview)
                .otherIncome(otherIncome)
                .training(training)
                .otherBenefit(otherBenefit)
                .jobDescription(jobDescription)
                .contactUserId(contactUserId)
                .inchargeHrNote(inchargeHrNote)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO).build();
    }

    public void onCreateRecruitmentDecisionSuccess(CreateRecruitmentDecisionSuccessReply reply) {
        log.info("onCreateRecruitmentDecisionSuccess {}", JSonMapper.toJson(reply));
        this.candidateResponse = reply.getCandidateResponse();
        this.decision = reply.getRecruitmentDecision();
        this.hrLeaders = reply.getHrLeaders();
    }

    public void onCreateRecruitmentDecisionFailure(CreateRecruitmentDecisionFailureReply reply) {
        log.info("onCreateRecruitmentDecisionFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public RecruitmentDecisionCreateRollbackCommand makeCreateRecruitmentDecisionRollbackCommand() {
        log.info("makeCreateRecruitmentDecisionRollbackCommand id {}, candidateId {}, contractTypeId {}, contractPeriodId {}, startWorkDate {}, officialSalary {}, probationarySalary {}, " +
                        "basicSalary {}, negotiableSalary {}, proposedSalary {}, workingPlaceId {}, workingTimeId {}, compensationBenefit {}, effortReview {}, otherIncome {}, training {}" +
                        ", otherBenefit {}, jobDescription {}, contactUserId {}, inchargeHrNote {}",
                JSonMapper.toJson(id), JSonMapper.toJson(candidateId), JSonMapper.toJson(contractTypeId), JSonMapper.toJson(contractPeriodId),
                JSonMapper.toJson(startWorkDate), JSonMapper.toJson(officialSalary), JSonMapper.toJson(probationarySalary), JSonMapper.toJson(basicSalary),
                JSonMapper.toJson(negotiableSalary), JSonMapper.toJson(proposedSalary), JSonMapper.toJson(workingPlaceId),
                JSonMapper.toJson(workingTimeId), JSonMapper.toJson(compensationBenefit), JSonMapper.toJson(effortReview),
                JSonMapper.toJson(otherIncome), JSonMapper.toJson(training), JSonMapper.toJson(otherBenefit),
                JSonMapper.toJson(jobDescription), JSonMapper.toJson(contactUserId), JSonMapper.toJson(inchargeHrNote));
        return RecruitmentDecisionCreateRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
    }


    public SendEmailCommand makeSendEmailCommand() {
        log.info("RecruitmentDecisionCreateState makeSendEmailCommand");
        List<String> candidateNameLst = new ArrayList<>();
        candidateNameLst.add(candidateResponse.getFullName());
        candidateNameLst.add(candidateResponse.getLevel() == null ? "" : candidateResponse.getLevel().getName());
        candidateNameLst.add(candidateResponse.getApplyPosition() == null ? "" : candidateResponse.getApplyPosition().getName());

        String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidateId).getBytes());
        String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);
        CandidateContent candidateContent = CandidateContent.builder()
                .link(link)
                .name(candidateResponse.getFullName())
                .build();
        Map<String, Object> mapContent = new HashMap<>();
        mapContent.put("inchargeHr", actionUserDTO.getFullName());
        mapContent.put("candidate", candidateContent);
        //Todo add proposalLink with candidate link to Test
        mapContent.put("decisionLink", link);
        List<String> listEmail = hrLeaders.stream().map(User::getEmail).collect(Collectors.toList());
        boolean isSendMail = true;
        if (CollectionUtils.isEmpty(listEmail)) {
            isSendMail = false;
        }
        String subjectCustom = MessageUtils.getMessage("email_title.recruitment_decision", candidateNameLst);
        return SendEmailCommand.builder()
                .isSendMail(isSendMail)
                .businessCode(BUSINESS_CODE)
                .ccEmails(new ArrayList<>())
                .toEmails(listEmail)
                .paramsContent(mapContent)
                .paramsSubject(candidateNameLst)
                .isCustomSubject(true)
                .subjectCustom(subjectCustom)
                .sysDate(sysDate).locale(locale).companyCid(companyCid).actionUserDTO(actionUserDTO).build();
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
