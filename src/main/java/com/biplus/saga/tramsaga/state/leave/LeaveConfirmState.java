package com.biplus.saga.tramsaga.state.leave;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.command.leave.LeaveConfirmCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.dto.leave.EmployeeLeaveDTO;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.leave.LeaveConfirmFailureReply;
import com.biplus.saga.domain.message.leave.LeaveConfirmSuccessReply;
import com.biplus.saga.domain.request.leave.ConfirmListOfLeavesRequest;
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
public class LeaveConfirmState extends BaseSagaState {
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private ConfirmListOfLeavesRequest request;

    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;
    private List<EmployeeLeaveDTO> data;

    public LeaveConfirmState(ConfirmListOfLeavesRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("LeaveConfirmState LeaveConfirmState {}", JSonMapper.toJson(request));
        log.info("LeaveConfirmState companyCid {}", companyCid);
    }

    public LeaveConfirmCommand makeLeaveConfirmCommand() {
        log.info("makeLeaveConfirmCommand");
        LeaveConfirmCommand command = new LeaveConfirmCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(LeaveConfirmSuccessReply reply) {
        log.info("LeaveConfirmSuccessReply {}", JSonMapper.toJson(reply));
        this.listMailInfo = reply.getListMailInfo();
        this.isSendMail = reply.getIsSendMail();
        this.data = reply.getData();

    }

    public void onFailure(LeaveConfirmFailureReply reply) {
        log.info("onLeaveConfirmFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendMultiEmailCommand makeSendMultiEmailCommand() {
        log.info("LeaveConfirmState makeSendEmailCommand");

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
        log.info("onSendEmailSuccess LeaveConfirmState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure LeaveConfirmState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand LeaveConfirmState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
