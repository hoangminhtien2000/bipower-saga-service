package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.recruitment.InterviewEvaluationCommand;
import com.biplus.saga.domain.command.recruitment.InterviewEvaluationRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.InterviewEvaluationFailureReply;
import com.biplus.saga.domain.message.recruitment.InterviewEvaluationSuccessReply;
import com.biplus.saga.domain.request.recruitment.SaveInterviewResultRequest;
import com.biplus.saga.domain.type.CandidateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEvaluationState extends BaseSagaState {

    private SaveInterviewResultRequest request;
    private boolean updateCandidateStatus;
    private boolean passInterview;

    private String businessCode = "SAVE_INTERVIEW_SCHEDULE";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    public InterviewEvaluationState(SaveInterviewResultRequest request) {
        this.sysDate = LocalDateTime.now();
        this.locale = LocaleContextHolder.getLocale();
        this.actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
    }

    @RollbackOnException
    public InterviewEvaluationCommand makeCommand() {
        InterviewEvaluationCommand command = InterviewEvaluationCommand.builder()
                .companyCid(this.companyCid)
                .sysDate(this.sysDate)
                .actionUserDTO(this.actionUserDTO)
                .locale(this.locale)
                .request(this.request)
                .build();
        log.info("make save interview evaluation command {}", command);
        return command;
    }

    public InterviewEvaluationRollbackCommand makeRollbackCommand() {
        InterviewEvaluationRollbackCommand rollbackCommand = new InterviewEvaluationRollbackCommand();
        BeanUtils.copyProperties(this, rollbackCommand);
        log.info("make save interview evaluation rollback command");
        return rollbackCommand;
    }

    public CandidateStatusUpdatingCommand makeUpdateCandidateStatusCommand() {
        CandidateStatusUpdatingCommand command = new CandidateStatusUpdatingCommand();
        BeanUtils.copyProperties(this, command);
        command.setIsUpdated(this.updateCandidateStatus);
        command.setCandidateId(this.request.getCandidateId());
        command.setCandidateStatus(passInterview ? CandidateStatus.R6.value() : CandidateStatus.R7_1.value());
        log.info("Make update candidate status command {}", command);
        return command;
    }

    public void onInterviewEvaluationSuccess(InterviewEvaluationSuccessReply reply) {
        log.info("Save interview result success {}", reply);
        this.updateCandidateStatus = reply.isUpdateCandidateStatus();
        this.passInterview = reply.isPassInterview();
    }

    public void onInterviewEvaluationFailure(InterviewEvaluationFailureReply reply) {
        log.info("Save interview result failure {}", reply);
        updateCandidateStatus = false;
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

}
