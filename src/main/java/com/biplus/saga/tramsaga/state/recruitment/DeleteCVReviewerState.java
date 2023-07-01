package com.biplus.saga.tramsaga.state.recruitment;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingRollbackCommand;
import com.biplus.saga.domain.command.recruitment.DeleteCVReviewerCommand;
import com.biplus.saga.domain.command.recruitment.DeleteCVReviewerRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.DeleteCVReviewerFailureReply;
import com.biplus.saga.domain.message.recruitment.DeleteCVReviewerSuccessReply;
import com.biplus.saga.domain.request.recruitment.DeleteCVReviewerRequest;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCVReviewerState extends BaseSagaState {
    private String webRootPath;
    private String candidateDetailLink;
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private Long id;
    private Long candidateId;
    private String candidateStatus;
    private Boolean isUpdated;
    public DeleteCVReviewerState(DeleteCVReviewerRequest request, String webRootPath, String candidateDetailLink) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.id = request.getId();
        this.webRootPath = webRootPath;
        this.candidateDetailLink = candidateDetailLink;
        log.info("DeleteCVReviewerState DeleteCVReviewerRequest {}", JSonMapper.toJson(request));
        log.info("DeleteCVReviewerState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public DeleteCVReviewerCommand makeDeleteCVReviewerCommand() {
        log.info("makeDeleteCVReviewerCommand id {}", JSonMapper.toJson(id));
        return DeleteCVReviewerCommand.builder().id(id).sysDate(sysDate).locale(locale).companyCid(companyCid).actionUserDTO(actionUserDTO).build();
    }

    public void onDeleteCVReviewerSuccess(DeleteCVReviewerSuccessReply reply) {
        log.info("onDeleteCVReviewerSuccess {}", JSonMapper.toJson(reply));
        this.isUpdated = reply.getIsUpdated();
        this.candidateStatus = reply.getCandidateStatus();
        this.candidateId = reply.getCandidateId();
    }

    public void onDeleteCVReviewerFailure(DeleteCVReviewerFailureReply reply) {
        log.info("onDeleteCVReviewerFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public DeleteCVReviewerRollbackCommand makeDeleteCVReviewerRollbackCommand() {
        log.info("makeDeleteCVReviewerRollbackCommand id {}", JSonMapper.toJson(id));
        return DeleteCVReviewerRollbackCommand.builder().sysDate(sysDate).locale(locale).companyCid(companyCid).build();
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
}
