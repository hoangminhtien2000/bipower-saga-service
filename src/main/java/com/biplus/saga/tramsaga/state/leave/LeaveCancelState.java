package com.biplus.saga.tramsaga.state.leave;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.command.leave.LeaveApproveCommand;
import com.biplus.saga.domain.command.leave.LeaveCancelCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.dto.leave.EmployeeLeaveDTO;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveApproveFailureReply;
import com.biplus.saga.domain.message.leave.LeaveApproveSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveCancelFailureReply;
import com.biplus.saga.domain.message.leave.LeaveCancelSuccessReply;
import com.biplus.saga.domain.request.leave.CancelLeaveRequest;
import com.biplus.saga.domain.request.leave.SendRequestLeavesRequest;
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
public class LeaveCancelState extends BaseSagaState {
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private CancelLeaveRequest request;

    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<EmployeeLeaveDTO> data;

    public LeaveCancelState(CancelLeaveRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("LeaveCancelState LeaveCancelState {}", JSonMapper.toJson(request));
        log.info("LeaveCancelState companyCid {}", companyCid);

    }

    @RollbackOnException
    public LeaveCancelCommand makeLeaveCancelCommand() {
        log.info("makeLeaveCancelCommand");
        LeaveCancelCommand command = new LeaveCancelCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(LeaveCancelSuccessReply reply) {
        log.info("LeaveCancelSuccessReply {}", JSonMapper.toJson(reply));
        this.listMailInfo = reply.getListMailInfo();
        this.isSendMail = reply.getIsSendMail();
        this.data = reply.getData();

    }

    public void onFailure(LeaveCancelFailureReply reply) {
        log.info("onLeaveCancelFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendMultiEmailCommand makeSendMultiEmailCommand() {
        log.info("LeaveCancelState makeSendEmailCommand");

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
        log.info("onSendEmailSuccess LeaveCancelState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure LeaveCancelState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand LeaveCancelState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
