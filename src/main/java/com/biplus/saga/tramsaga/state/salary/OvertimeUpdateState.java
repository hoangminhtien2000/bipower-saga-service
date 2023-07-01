package com.biplus.saga.tramsaga.state.salary;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.salary.OvertimeCreateCommand;
import com.biplus.saga.domain.command.salary.OvertimeUpdateCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeCreateFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeCreateSuccessReply;
import com.biplus.saga.domain.message.salary.OvertimeUpdateFailureReply;
import com.biplus.saga.domain.message.salary.OvertimeUpdateSuccessReply;
import com.biplus.saga.domain.request.salary.UpdateOvertimeRequest;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeUpdateState extends BaseSagaState {
    private String businessCode = "SALARY_UPDATE_OVERTIME";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private UpdateOvertimeRequest request;

    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private List<String> paramsSubject;

    public OvertimeUpdateState(UpdateOvertimeRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("OvertimeUpdateState OvertimeUpdateState {}", JSonMapper.toJson(request));
        log.info("OvertimeUpdateState companyCid {}", companyCid);

    }
    @RollbackOnException
    public OvertimeUpdateCommand makeOvertimeUpdateCommand() {
        log.info("makeOvertimeUpdateCommand");
        OvertimeUpdateCommand command = new OvertimeUpdateCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;

    }

    public void onSuccess(OvertimeUpdateSuccessReply reply) {
        log.info("onOvertimeUpdateSuccess {}", JSonMapper.toJson(reply));
        this.businessCode = reply.getBusinessCode();
        this.toEmails = reply.getToEmails();
        this.parameters = reply.getParameters();
        this.ccEmails = reply.getCcEmails();
        this.isSendMail = reply.getIsSendMail();
        this.paramsSubject = reply.getParamsSubject();
    }

    public void onFailure(OvertimeUpdateFailureReply reply) {
        log.info("onOvertimeUpdateFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeSalaryRollbackCommand OvertimeUpdateState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();

    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("OvertimeUpdateState makeSendEmailCommand");
        return SendEmailCommand.builder()
                .businessCode(businessCode)
                .ccEmails(new ArrayList<>())
                .toEmails(toEmails)
                .paramsContent(parameters)
                .paramsSubject(paramsSubject)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .isSendMail(isSendMail)
                .build();
    }

    public void onSendEmailSuccess(SendEmailSuccessReply reply) {
        log.info("onSendEmailSuccess OvertimeUpdateState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure OvertimeUpdateState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }
}
