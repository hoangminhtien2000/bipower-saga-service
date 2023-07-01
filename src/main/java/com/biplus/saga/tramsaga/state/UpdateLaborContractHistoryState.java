package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.contract.UpdateLaborContractHistoryCommand;
import com.biplus.saga.domain.command.contract.ContractRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.employee.UpdateLaborContractTypeCommand;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistoryFailureReply;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistorySuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeFailureReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeSuccessReply;
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
public class UpdateLaborContractHistoryState extends BaseSagaState {
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    private Long laborContractId;
    private LaborContractHistoryDTO laborContractHistoryDTO;
    private List<String> listRoleOfCurrentUser;

    // Contract
    private Long employeeId;
    private String laborContractType;
    private String laborContractStatus;
    private Long netSalary;

    //Email
    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private String businessCode;
    private ActionUserDTO actionUserDTO;
    private Boolean isSendMail;

    public UpdateLaborContractHistoryState(Long laborContractId, LaborContractHistoryDTO laborContractHistoryDTO, List<String> listRoleCurrentUser) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        this.laborContractId = laborContractId;
        this.laborContractHistoryDTO = laborContractHistoryDTO;
        this.listRoleOfCurrentUser = listRoleCurrentUser;
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.actionUserDTO = ActionUserHolder.getActionUser();
        log.info("UpdateLaborContractHistoryState laborContractHistoryDTO {}", JSonMapper.toJson(laborContractHistoryDTO));
        log.info("UpdateLaborContractHistoryState laborContractId {}", laborContractId);
        log.info("UpdateLaborContractHistoryState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public UpdateLaborContractHistoryCommand makeUpdateLaborContractHistoryCommand() {
        log.info("makeUpdateLaborContractHistoryCommand laborContractHistoryDTO {}", JSonMapper.toJson(laborContractHistoryDTO));
        return UpdateLaborContractHistoryCommand.builder()
                .laborContractId(laborContractId)
                .sysDate(sysDate)
                .locale(locale)
                .laborContractHistoryDTO(laborContractHistoryDTO)
                .listRoleOfCurrentUser(listRoleOfCurrentUser)
                .companyCid(companyCid)
                .build();
    }

    public void onUpdateLaborContractHistorySuccess(UpdateLaborContractHistorySuccessReply reply) {
        log.info("UpdateLaborContractHistorySuccessReply {}", JSonMapper.toJson(reply));
        this.employeeId = reply.getEmployeeId();
        this.laborContractType = reply.getLaborContractType();
        this.laborContractStatus = reply.getLaborContractStatus();
        this.netSalary = reply.getNetSalary();

        this.ccEmails = reply.getCcEmails();
        this.toEmails = reply.getToEmails();
        this.businessCode = reply.getBusinessCode();
        this.parameters = reply.getParameters();
        this.isSendMail = reply.getIsSendMail();
    }

    public void onUpdateLaborContractHistoryFailure(UpdateLaborContractHistoryFailureReply reply) {
        log.info("UpdateLaborContractHistoryFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public ContractRollbackCommand makeUpdateLaborContractHistoryRollbackCommand() {
        log.info("makeUpdateLaborContractHistoryRollbackCommand laborContractHistoryDTO {}", JSonMapper.toJson(laborContractHistoryDTO));
        return ContractRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }

    public EmployeeRollbackCommand makeUpdateEmployeeRollbackCommand() {
        log.info("makeUpdateLaborContractHistoryRollbackCommand laborContractHistoryDTO {}", JSonMapper.toJson(laborContractHistoryDTO));
        return EmployeeRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }


    public UpdateLaborContractTypeCommand makeUpdateLaborContractTypeCommand() {
        log.info("makeUpdateLaborContractTypeCommand laborContractHistoryDTO {}", JSonMapper.toJson(laborContractHistoryDTO));
        return UpdateLaborContractTypeCommand.builder()
                .employeeId(employeeId)
                .laborContractType(laborContractType)
                .laborContractStatus(laborContractStatus)
                .netSalary(netSalary)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }


    public void onUpdateLaborContractTypeSuccess(UpdateLaborContractTypeSuccessReply reply) {
        log.info("UpdateLaborContractTypeSuccessReply {}", JSonMapper.toJson(reply));
        this.employeeId = reply.getEmployeeId();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("EmployeeCreateState makeSendEmailCommand");

        return SendEmailCommand.builder()
                .businessCode(businessCode)
                .ccEmails(new ArrayList<>())
                .toEmails(toEmails)
                .paramsContent(parameters)
                .paramsSubject(new ArrayList<>())
                .isSendMail(isSendMail)
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .actionUserDTO(actionUserDTO)
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

    public void onUpdateLaborContractTypeFailure(UpdateLaborContractTypeFailureReply reply) {
        log.info("UpdateLaborContractTypeFailureReply {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

}
