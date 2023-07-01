package com.biplus.saga.tramsaga.state.employee;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.employee.EmployeeCreateCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateSuccessReply;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateState extends BaseSagaState {
    private String businessCode = "CREATE_EMPLOYEE";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private CreateEmployeeRequest request;

    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;


    public EmployeeCreateState(CreateEmployeeRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        log.info("EmployeeCreateState CreateEmployeeRequest {}", JSonMapper.toJson(request));
        log.info("EmployeeCreateState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public EmployeeCreateCommand makeEmployeeCreateCommand() {
        log.info("makeEmployeeCreateCommand");
        EmployeeCreateCommand employeeCreateCommand = new EmployeeCreateCommand();
        employeeCreateCommand.setRequest(request);
        employeeCreateCommand.setActionUserDTO(actionUserDTO);
        employeeCreateCommand.setLocale(locale);
        employeeCreateCommand.setSysDate(sysDate);
        employeeCreateCommand.setCompanyCid(companyCid);
        return employeeCreateCommand;
    }

    public void onEmployeeCreateSuccess(EmployeeCreateSuccessReply reply) {
        log.info("onEmployeeCreateSuccess {}", JSonMapper.toJson(reply));
        this.businessCode = reply.getBusinessCode();
        this.toEmails = reply.getToEmails();
        this.parameters = reply.getParameters();
        this.ccEmails = reply.getCcEmails();
        this.isSendMail = reply.getIsSendMail();

    }

    public void onEmployeeCreateFailure(EmployeeCreateFailureReply reply) {
        log.info("onEmployeeCreateFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public EmployeeRollbackCommand makeEmployeeCreateRollbackCommand() {
        log.info("makeEmployeeCreateRollbackCommand create employee: {}", request);
        return EmployeeRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("EmployeeCreateState makeSendEmailCommand");

        return SendEmailCommand.builder()
                .businessCode(businessCode)
                .ccEmails(new ArrayList<>())
                .toEmails(toEmails)
                .paramsContent(parameters)
                .paramsSubject(new ArrayList<>())
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
                .isSendMail(isSendMail)
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
