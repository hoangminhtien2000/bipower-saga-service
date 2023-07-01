package com.biplus.saga.domain.command.employee;

import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.command.TenantAware;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.employee.domain.command.UpdateLaborContractTypeCommand")
public class UpdateLaborContractTypeCommand implements CompensatableCommand, TenantAware {
    private Long employeeId;
    private String laborContractType;
    private String laborContractStatus;
    private Long netSalary;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
}
