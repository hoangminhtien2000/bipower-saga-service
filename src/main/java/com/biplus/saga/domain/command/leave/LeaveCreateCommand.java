package com.biplus.saga.domain.command.leave;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import com.biplus.saga.domain.request.leave.LeaveRegistrationRequest;
import com.biplus.saga.domain.request.salary.CreateOvertimeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.salary.domain.command.LeaveCreateCommand")
public class LeaveCreateCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
    private LeaveRegistrationRequest request;
    private Boolean isSendMail;
}
