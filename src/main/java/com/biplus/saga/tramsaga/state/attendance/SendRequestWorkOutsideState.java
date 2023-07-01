package com.biplus.saga.tramsaga.state.attendance;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.attendance.SendRequestWorkOutsideCommand;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.dto.attendance.AttendanceHistoryDTO;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.request.attendance.SendRequestAttendanceReq;
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
public class SendRequestWorkOutsideState extends BaseSagaState {
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private SendRequestAttendanceReq request;

    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<AttendanceHistoryDTO> data;

    public SendRequestWorkOutsideState(SendRequestAttendanceReq request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("SendRequestWorkOutsideState SendRequestWorkOutsideState {}", JSonMapper.toJson(request));
        log.info("SendRequestWorkOutsideState companyCid {}", companyCid);

    }

    @RollbackOnException
    public SendRequestWorkOutsideCommand makeSendRequestWorkOutsideCommand() {
        log.info("makeSendRequestWorkOutsideCommand");
        SendRequestWorkOutsideCommand command = new SendRequestWorkOutsideCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(SendRequestWorkOutsideSuccessReply reply) {
        log.info("SendRequestWorkOutsideSuccessReply {}", JSonMapper.toJson(reply));
        this.listMailInfo = reply.getListMailInfo();
        this.isSendMail = reply.getIsSendMail();
        this.data = reply.getData();

    }

    public void onFailure(SendRequestWorkOutsideFailureReply reply) {
        log.info("SendRequestWorkOutsideFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendMultiEmailCommand makeSendMultiEmailCommand() {
        log.info("SendRequestWorkOutsideState makeSendEmailCommand");

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
        log.info("onSendEmailSuccess SendRequestWorkOutsideState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure SendRequestWorkOutsideState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand SendRequestWorkOutsideState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
