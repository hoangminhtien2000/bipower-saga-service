package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.candidate.CandidateAssignCommand;
import com.biplus.saga.domain.command.candidate.CandidateAssignRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.dto.UserDTO;
import com.biplus.saga.domain.dto.candidate.Candidate;
import com.biplus.saga.domain.dto.candidate.CandidateContent;
import com.biplus.saga.domain.message.candidate.CandidateAssignFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateAssignSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateAssignState extends BaseSagaState {
    private final static String BUSINESS_CODE = "ASSIGN_CANDIDATE";
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private List<Long> candidateIds;
    private Long inChargeUserId;
    private String note;
    private List<Candidate> candidateList;
    private UserDTO assigner;

    public CandidateAssignState(AssignCandidateRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.candidateIds = request.getCandidateIds();
        this.inChargeUserId = request.getInChargeUserId();
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
    public CandidateAssignCommand makeCandidateAssignCommand() {
        log.info("makeCandidateAssignCommand candidateIds {}, inChargeUserId {}, note {}", JSonMapper.toJson(candidateIds), JSonMapper.toJson(inChargeUserId), JSonMapper.toJson(note));
        return CandidateAssignCommand.builder()
                .candidateIds(candidateIds)
                .inChargeUserId(inChargeUserId)
                .note(note)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .build();
    }

    public void onCandidateAssignSuccess(CandidateAssignSuccessReply reply) {
        log.info("onCandidateAssignSuccess {}", JSonMapper.toJson(reply));
        this.candidateList = reply.getCandidateList();
        this.assigner = reply.getAssigner();
    }

    public void onCandidateAssignFailure(CandidateAssignFailureReply reply) {
        log.info("onCandidateAssignFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public CandidateAssignRollbackCommand makeCandidateAssignRollbackCommand() {
        log.info("makeCandidateAssignRollbackCommand candidateIds {}, inChargeUserId {}, note {}", JSonMapper.toJson(candidateIds), JSonMapper.toJson(inChargeUserId), JSonMapper.toJson(note));
        return CandidateAssignRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("CandidateAssignState makeSendEmailCommand");
        int sizeCandidate = this.candidateList.size();
        List<CandidateContent> candidateContents = new ArrayList<>();
        List<String> candidateNameLst = new ArrayList<>();
        for (Candidate candidate : candidateList) {
            String encodedString = Base64.getEncoder().encodeToString(String.valueOf(candidate.getId()).getBytes());
            String link = String.format("%s%s?%s%s", webRootPath, candidateDetailLink, "q=", encodedString);
            CandidateContent candidateContent = new CandidateContent();
            candidateContent.setLink(link);
            candidateContent.setName(candidate.getFullName());
            candidateContents.add(candidateContent);
            candidateNameLst.add(candidate.getFullName());
        }
        Map<String, Object> mapContent = new HashMap<>();
        mapContent.put("size", String.valueOf(sizeCandidate));
        mapContent.put("candidateList", candidateContents);
        mapContent.put("note", note);
        mapContent.put("assigner", assigner.getFullName());
        List<String> listEmail = new ArrayList<String>();
        listEmail.add(assigner.getEmail());
        return SendEmailCommand.builder()
                .businessCode(BUSINESS_CODE)
                .ccEmails(new ArrayList<>())
                .toEmails(listEmail)
                .paramsContent(mapContent)
                .paramsSubject(candidateNameLst)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
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
