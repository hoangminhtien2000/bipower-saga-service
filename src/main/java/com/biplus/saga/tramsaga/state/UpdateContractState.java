package com.biplus.saga.tramsaga.state;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.contract.UpdateContractCommand;
import com.biplus.saga.domain.command.contract.ContractRollbackCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.employee.UpdateLaborContractTypeCommand;
import com.biplus.saga.domain.message.contract.UpdateContractFailureReply;
import com.biplus.saga.domain.message.contract.UpdateContractSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeFailureReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeSuccessReply;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;
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
public class UpdateContractState extends BaseSagaState {
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;

    // Contract
    private UpdateLaborContractRequest request;

    //Email
    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private String businessCode;
    private ActionUserDTO actionUserDTO;
    private Boolean isSendMail;

    public UpdateContractState(UpdateLaborContractRequest request) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        this.request = request;
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.actionUserDTO = ActionUserHolder.getActionUser();
        log.info("UpdateContractState laborContractHistoryDTO {}", JSonMapper.toJson(request));
        log.info("UpdateContractState companyCid {}", companyCid);
    }

    /*
     * Contract Service Command
     */

    @RollbackOnException
    public UpdateContractCommand makeUpdateLaborContractHistoryCommand() {
        log.info("makeUpdateLaborContractHistoryCommand laborContractHistoryDTO {}", JSonMapper.toJson(request));
        return UpdateContractCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .request(request)
                .actionUserDTO(actionUserDTO)
                .companyCid(companyCid)
                .build();
    }

    public void onUpdateContractSuccess(UpdateContractSuccessReply reply) {
        log.info("onUpdateContractSuccess {}", JSonMapper.toJson(reply));
        this.ccEmails = reply.getCcEmails();
        this.toEmails = reply.getToEmails();
        this.businessCode = reply.getBusinessCode();
        this.parameters = reply.getParameters();
        this.isSendMail = reply.getIsSendMail();
    }

    public void onUpdateContractFailure(UpdateContractFailureReply reply) {
        log.info("onUpdateContractFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }


    public ContractRollbackCommand makeUpdateContractRollbackCommand() {
        log.info("makeUpdateLaborContractHistoryRollbackCommand laborContractHistoryDTO {}", JSonMapper.toJson(request));
        return ContractRollbackCommand.builder()
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

}
