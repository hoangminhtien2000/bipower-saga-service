package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.MessageUtils;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.recruitment.CreateRecruitmentProposalCommand;
import com.biplus.saga.domain.command.recruitment.CreateRecruitmentProposalRollbackCommand;
import com.biplus.saga.domain.dto.candidate.CandidateContent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentProposalFailureReply;
import com.biplus.saga.domain.message.recruitment.CreateRecruitmentProposalSuccessReply;
import com.biplus.saga.domain.request.recruitment.CreatingRecruitmentProposalRequest;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.EmployeeDataResponse;
import com.biplus.saga.domain.response.RecruitmentProposalResponse;
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
public class RecruitmentProposalCreateState extends BaseSagaState {
    private final static String BUSINESS_CODE = "RECRUITMENT_PROPOSAL";
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private Long id;
    private Long candidateId;
    private String netSalary;
    private Long applyPositionId;
    private Long teamId;
    private Double productivity;
    private String project;
    private String hrNote;

    private CandidateResponse candidateResponse;
    private RecruitmentProposalResponse proposal;
    private List<User> hrLeaders;

    public RecruitmentProposalCreateState(CreatingRecruitmentProposalRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.id = request.getId();
        this.candidateId = request.getCandidateId();
        this.netSalary = request.getNetSalary();
        this.applyPositionId = request.getApplyPositionId();
        this.teamId = request.getTeamId();
        this.productivity = request.getProductivity();
        this.project = request.getProject();
        this.hrNote = request.getHrNote();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("RecruitmentProposalCreateState CreatingRecruitmentProposalRequest {}", JSonMapper.toJson(request));
        log.info("RecruitmentProposalCreateState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public CreateRecruitmentProposalCommand makeCreateRecruitmentProposalCommand() {
        log.info("makeCreateRecruitmentProposalCommand id {}, candidateId {}, netSalary {}, applyPosition {}, teamId {}, productivity {}, project {}, " +
                        "hrNote {}",
                JSonMapper.toJson(id), JSonMapper.toJson(candidateId), JSonMapper.toJson(netSalary), JSonMapper.toJson(applyPositionId), JSonMapper.toJson(teamId),
                JSonMapper.toJson(productivity), JSonMapper.toJson(project), JSonMapper.toJson(hrNote));
        return CreateRecruitmentProposalCommand.builder()
                .id(id)
                .candidateId(candidateId)
                .netSalary(netSalary)
                .applyPositionId(applyPositionId)
                .teamId(teamId)
                .productivity(productivity)
                .project(project)
                .hrNote(hrNote)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO).build();
    }

    public void onCreateRecruitmentProposalSuccess(CreateRecruitmentProposalSuccessReply reply) {
        log.info("onCreateRecruitmentProposalSuccess {}", JSonMapper.toJson(reply));
        this.candidateId = reply.getCandidateId();
        this.candidateResponse = reply.getCandidateResponse();
        this.proposal = reply.getProposal();
        this.hrLeaders = reply.getHrLeaders();
    }

    public void onCreateRecruitmentProposalFailure(CreateRecruitmentProposalFailureReply reply) {
        log.info("onCreateRecruitmentProposalFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public CreateRecruitmentProposalRollbackCommand makeCreateRecruitmentProposalRollbackCommand() {
        log.info("makeCreateRecruitmentProposalRollbackCommand id {}, candidateId {}, netSalary {}, applyPosition {}, teamId {}, productivity {}, project {}, " +
                        "hrNote {}",
                JSonMapper.toJson(id), JSonMapper.toJson(candidateId), JSonMapper.toJson(netSalary), JSonMapper.toJson(applyPositionId), JSonMapper.toJson(teamId),
                JSonMapper.toJson(productivity), JSonMapper.toJson(project), JSonMapper.toJson(hrNote));
        return CreateRecruitmentProposalRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
    }

   /* @RollbackOnException
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
    }*/

    public SendEmailCommand makeSendEmailCommand() {
        log.info("RecruitmentProposalCreateState makeSendEmailCommand");
        List<String> candidateNameLst = new ArrayList<>();
        candidateNameLst.add(candidateResponse.getFullName());
        candidateNameLst.add(candidateResponse.getApplyPosition() == null ? "": candidateResponse.getApplyPosition().getName());

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
        mapContent.put("proposalLink", link);
        List<String> listEmail = hrLeaders.stream().map(User::getEmail).collect(Collectors.toList());
        boolean isSendMail = true;
        if (CollectionUtils.isEmpty(listEmail)) {
            isSendMail = false;
        }
        String subjectCustom = MessageUtils.getMessage("email_title.recruitment_proposal", candidateNameLst);
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
