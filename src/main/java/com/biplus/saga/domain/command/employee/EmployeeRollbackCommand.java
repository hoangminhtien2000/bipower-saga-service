package com.biplus.saga.domain.command.employee;

import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.employee.domain.command.EmployeeRollbackCommand")
public class EmployeeRollbackCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
}
