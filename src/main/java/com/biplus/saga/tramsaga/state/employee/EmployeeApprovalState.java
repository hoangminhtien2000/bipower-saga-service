package com.biplus.saga.tramsaga.state.employee;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.employee.EmployeeApprovalCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.team.TeamAssignRoleAndTeamForEmployeeCommand;
import com.biplus.saga.domain.command.team.TeamRollbackCommand;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeApprovalFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeApprovalSuccessReply;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeFailureReply;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeSuccessReply;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
public class EmployeeApprovalState extends BaseSagaState {
    private String businessCode = "APPROVAL_EMPLOYEE";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    //employee request
    private EmployeeHistoryRequest request;
    //team
    private Boolean isAssignTeamAndRole;


    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private String companyEmail;

    public EmployeeApprovalState(EmployeeHistoryRequest request) {
        this.request = request;
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        log.info("EmployeeHistoryRequest {}", JSonMapper.toJson(request));
        log.info("EmployeeApprovalState companyCid {}", companyCid);
    }

    @RollbackOnException
    public EmployeeApprovalCommand makeCommand() {
        log.info("makeEmployeeApprovalCommand");
        EmployeeApprovalCommand command = new EmployeeApprovalCommand();
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(EmployeeApprovalSuccessReply reply) {
        log.info("onEmployeeApprovalSuccess {}", JSonMapper.toJson(reply));
        this.businessCode = reply.getBusinessCode();
        this.toEmails = reply.getToEmails();
        this.parameters = reply.getParameters();
        this.ccEmails = reply.getCcEmails();
        this.isSendMail = reply.getIsSendMail();
        this.isAssignTeamAndRole = reply.getIsAssignTeamAndRole();
        this.companyEmail = reply.getCompanyEmail();

    }

    public void onFailure(EmployeeApprovalFailureReply reply) {
        log.info("onEmployeeApprovalFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public EmployeeRollbackCommand makeRollbackCommand() {
        log.info("EmployeeApprovalRollbackCommand create employee: {}", request);
        return EmployeeRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("EmployeeApproval makeSendEmailCommand");

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

    public TeamAssignRoleAndTeamForEmployeeCommand makeAssignTeamAndRoleCommand() {
        log.info("makeEmployeeApprovalCommand");
        TeamAssignRoleAndTeamForEmployeeCommand command = new TeamAssignRoleAndTeamForEmployeeCommand();
        command.setEmployeeId(request.getEmployeeId());
        command.setTeams(request.getTeams());
        command.setIsAssignTeamAndRole(isAssignTeamAndRole);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        command.setCompanyEmail(companyEmail);
        return command;
    }

    public void onAssignTeamAndRoleSuccess(TeamAssignRoleAndTeamForEmployeeSuccessReply reply) {
        log.info("onAssignTeamAndRoleSuccess {}", JSonMapper.toJson(reply));
    }

    public void onAssignTeamAndRoleFailure(TeamAssignRoleAndTeamForEmployeeFailureReply reply) {
        log.info("onAssignTeamAndRoleFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public TeamRollbackCommand makeAssignTeamAndRoleRollbackCommand() {
        log.info("EmployeeApprovalRollbackCommand create employee: {}", request);
        return TeamRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
