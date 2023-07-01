package com.biplus.saga.tramsaga.state.attendance;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.annotation.RollbackOnException;
import com.biplus.core.tram.saga.BaseSagaState;
import com.biplus.saga.domain.command.attendance.WorkOutsideUpdateCommand;
import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.request.attendance.CreateOrUpdateWorkOutsideReq;
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
public class WorkOutsideUpdateState extends BaseSagaState {
    private String businessCode = "SALARY_UPDATE_WORK_OUTSIDE";
    private ActionUserDTO actionUserDTO;
    private LocalDateTime sysDate;
    private Locale locale;
    private String companyCid;
    private CreateOrUpdateWorkOutsideReq request;
    private Long attendanceId;

    private Map<String, Object> parameters;
    private List<String> toEmails;
    private List<String> ccEmails;
    private Boolean isSendMail;
    private List<String> paramsSubject;

    public WorkOutsideUpdateState(CreateOrUpdateWorkOutsideReq request, Long attendanceId) {
        sysDate = LocalDateTime.now();
        locale = LocaleContextHolder.getLocale();
        actionUserDTO = ActionUserHolder.getActionUser();
        this.companyCid = ActionUserHolder.getActionUser().getCompanyCid();
        this.request = request;
        this.attendanceId = attendanceId;
        log.info("WorkOutsideUpdateState WorkOutsideUpdateState {}", JSonMapper.toJson(request));
        log.info("WorkOutsideUpdateState companyCid {}", companyCid);
    }

    @RollbackOnException
    public WorkOutsideUpdateCommand makeWorkOutsideUpdateCommand() {
        log.info("makeWorkOutsideUpdateCommand");
        WorkOutsideUpdateCommand command = new WorkOutsideUpdateCommand();
        command.setAttendanceId(attendanceId);
        command.setRequest(request);
        command.setActionUserDTO(actionUserDTO);
        command.setLocale(locale);
        command.setSysDate(sysDate);
        command.setCompanyCid(companyCid);
        return command;
    }

    public void onSuccess(WorkOutsideUpdateSuccessReply reply) {
        log.info("onWorkOutsideUpdateSuccess {}", JSonMapper.toJson(reply));
        this.businessCode = reply.getBusinessCode();
        this.toEmails = reply.getToEmails();
        this.parameters = reply.getParameters();
        this.ccEmails = reply.getCcEmails();
        this.isSendMail = reply.getIsSendMail();
        this.paramsSubject =reply.getParamsSubject();
    }

    public void onFailure(WorkOutsideUpdateFailureReply reply) {
        log.info("onWorkOutsideUpdateFailure {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SendEmailCommand makeSendEmailCommand() {
        log.info("WorkOutsideUpdateState makeSendEmailCommand");

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
        log.info("onSendEmailSuccess WorkOutsideUpdateState {}", JSonMapper.toJson(reply));
    }

    public void onSendEmailFailure(SendEmailFailureReply reply) {
        log.info("onSendEmailFailure WorkOutsideUpdateState {}", reply);
        this.errorCode = reply.getErrorCode();
        this.errorMessage = reply.getMessage();
    }

    public SalaryRollbackCommand makeSalaryRollbackCommand() {
        log.info("makeWorkOutsideRollbackCommand WorkOutsideUpdateState: {}", request);
        return SalaryRollbackCommand.builder()
                .sysDate(sysDate)
                .locale(locale)
                .companyCid(companyCid)
                .build();
    }
}
