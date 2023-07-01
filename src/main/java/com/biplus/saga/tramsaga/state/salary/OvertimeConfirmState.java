package com.biplus.saga.tramsaga.state.salary;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.command.salary.OvertimeApproveCommand;
import com.biplus.saga.domain.command.salary.OvertimeConfirmCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.dto.email.EmailInfoDTO;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeApproveFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeApproveSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeConfirmFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeConfirmSuccessReply;
import com.biplus.saga.domain.request.salary.ConfirmManyOvertimeRequest;
import com.biplus.saga.domain.request.salary.OvertimeToApproverRequest;
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
public class OvertimeConfirmState extends BaseSagaState {
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private ConfirmManyOvertimeRequest request;

    private List<EmailInfoDTO> listMailInfo;
    private Boolean isSendMail;

    public OvertimeConfirmState(ConfirmManyOvertimeRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("OvertimeConfirmState OvertimeConfirmState {}", JSonMapper.toJson(request));
        log.info("OvertimeConfirmState companyCid {}", companyCid);
    }

    public OvertimeConfirmCommand makeOvertimeConfirmCommand() {
        log.info("makeOvertimeConfirmCommand");
        OvertimeConfirmCommand command = new OvertimeConfirmCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(OvertimeConfirmSuccessReply reply) {
        log.info("onOvertimeApproveSuccess {}", JSonMapper.toJson(reply));
        this.listMailInfo = reply.getListMailInfo();
        this.isSendMail = reply.getIsSendMail();

    }

    public void onFailure(OvertimeConfirmFailureReply reply) {
        log.info("onApproveFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendMultiEmailCommand makeSendMultiEmailCommand() {
        log.info("OvertimeConfirmState makeSendEmailCommand");

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
        log.info("onSendEmailSuccess OvertimeConfirmState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure OvertimeConfirmState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand OvertimeConfirmState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

}
