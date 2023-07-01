package com.biplus.saga.domain.command.contract;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.contract.domain.command.UpdateContractCommand")
public class UpdateContractCommand implements CompensatableCommand, Serializable {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
    private UpdateLaborContractRequest request;

}
