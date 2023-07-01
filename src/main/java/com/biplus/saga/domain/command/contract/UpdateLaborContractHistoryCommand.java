package com.biplus.saga.domain.command.contract;

import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.contract.domain.command.UpdateLaborContractHistoryCommand")
public class UpdateLaborContractHistoryCommand implements CompensatableCommand, Serializable {
    private Long laborContractId;
    private LaborContractHistoryDTO laborContractHistoryDTO;
    private List<String> listRoleOfCurrentUser;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
}
