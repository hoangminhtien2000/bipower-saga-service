package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.enums.ErrorCode;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.common.DateUtil;
import com.biplus.saga.domain.command.candidate.CandidateStatusUpdatingCommand;
import com.biplus.saga.domain.command.recruitment.SaveInterviewContactCommand;
import com.biplus.saga.domain.command.recruitment.SaveInterviewContactRollbackCommand;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewContactFailureReply;
import com.biplus.saga.domain.message.recruitment.SaveInterviewContactSuccessReply;
import com.biplus.saga.domain.request.recruitment.SaveInterviewContactRequest;
import com.biplus.saga.domain.type.CandidateStatus;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveInterviewContactState extends BaseSagaState {

    private String businessCode = "SAVE_INTERVIEW_CONTACT";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    private Long candidateId;
    private LocalDateTime contactTime;
    private Integer candidateResponse;
    private String note;

    public SaveInterviewContactState(SaveInterviewContactRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        candidateId = request.getCandidateId();
        candidateResponse = request.getCandidateResponse();
        note = request.getNote();
        contactTime = DateUtil.parseLocalDateTime(request.getContactTime(), "CONTACT_TIME_INVALID");
        log.info("Save interview contact state from request {}", JSonMapper.toJson(request));
    }

    @RollbackOnException
    public SaveInterviewContactCommand makeSaveInterviewContactCommand() {
        log.info("makeSaveInterviewContactCommand to chanel {}", ServiceChannel.RECRUITMENT_CHANNEL);
        SaveInterviewContactCommand command = new SaveInterviewContactCommand();
        command.setCandidateId(candidateId);
        command.setCandidateResponse(candidateResponse);
        command.setNote(note);
        command.setContactTime(contactTime);

        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public SaveInterviewContactRollbackCommand makeSaveInterviewContactRollbackCommand() {
        log.info("save interview contact rollback command {}", this);
        return SaveInterviewContactRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    public CandidateStatusUpdatingCommand makeCandidateUpdatingCommand() {
        log.info("MakeCandidateStatusUpdatingCommand");
        return CandidateStatusUpdatingCommand.builder()
                .sysDate(sysDate)
                .actionUserDTO(actionUserDTO)
                .locale(locale)
                .companyCid(companyCid)
                .candidateId(candidateId)
                .isUpdated(true)
                .candidateStatus(candidateResponse != null &&candidateResponse == 1
                        ? CandidateStatus.R5_2.value()
                        : CandidateStatus.R6_1.value())
                .build();
    }

    public void onSaveInterviewContactSuccess(SaveInterviewContactSuccessReply reply) {
        log.info("Save interview contact reply: {}", reply);
    }

    public void onSaveInterviewContactFailure(SaveInterviewContactFailureReply reply) {
        log.info("Save interview contact failure: {} - {}", reply.getErrorCode(), reply.getMessage());
        errorCode = reply.getErrorCode();
        errorMessage = reply.getMessage();
    }

    public void onChangeCandidateStatusSuccess(CandidateUpdateStatusSuccessReply reply) {
        log.info("Change candidate status success {}", reply);
    }

    public void onChangeCandidateStatusFailure(CandidateUpdateStatusFailureReply reply) {
        log.info("Change candidate status failure {} - {}", reply.getErrorCode(), reply.getMessage());
        errorCode = reply.getErrorCode();
        errorMessage = reply.getMessage();
    }

}
