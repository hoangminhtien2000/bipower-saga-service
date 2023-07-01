package com.biplus.saga.tramsaga.state.attendance;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.attendance.ConfirmWorkOutsideCommand;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.dto.attendance.AttendanceHistoryDTO;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmWorkOutsideState extends BaseSagaState {
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private ConfirmManyRequestAttendanceReq request;

    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<AttendanceHistoryDTO> data;

    public ConfirmWorkOutsideState(ConfirmManyRequestAttendanceReq request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("ConfirmWorkOutsideState ConfirmWorkOutsideState {}", JSonMapper.toJson(request));
        log.info("ConfirmWorkOutsideState companyCid {}", companyCid);

    }

    @RollbackOnException
    public ConfirmWorkOutsideCommand makeConfirmWorkOutsideCommand() {
        log.info("makeConfirmManyRequestEditWorkingTimeCommand");
        ConfirmWorkOutsideCommand command = new ConfirmWorkOutsideCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(ConfirmWorkOutsideSuccessReply reply) {
        log.info("ConfirmWorkOutsideSuccessReply {}", JSonMapper.toJson(reply));
        this.listMailInfo = reply.getListMailInfo();
        this.isSendMail = reply.getIsSendMail();
        this.data = reply.getData();

    }

    public void onFailure(ConfirmWorkOutsideFailureReply reply) {
        log.info("ConfirmWorkOutsideFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendMultiEmailCommand makeSendMultiEmailCommand() {
        log.info("ConfirmWorkOutsideState makeSendEmailCommand");

        return SendMultiEmailCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .listMailInfo(listMailInfo)
                .isSendMail(isSendMail)
                .build();
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("onSendEmailSuccess ConfirmWorkOutsideState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure ConfirmWorkOutsideState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand ConfirmWorkOutsideState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
