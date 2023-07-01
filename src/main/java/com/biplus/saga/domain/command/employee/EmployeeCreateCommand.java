package com.biplus.saga.domain.command.employee;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
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
@TargetMessage("com.biplus.employee.domain.command.EmployeeCreateCommand")
public class EmployeeCreateCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
    private CreateEmployeeRequest request;
    private Boolean isSendMail;
}
