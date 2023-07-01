package com.biplus.saga.domain.command.recruitment;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.RetryableCommand;
import com.biplus.core.tram.command.TenantAware;
import com.biplus.core.tram.producer.TargetMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@TargetMessage("com.biplus.recruitment.domain.command.RecruitmentDecisionCreateRollbackCommand")
public class RecruitmentDecisionCreateRollbackCommand implements RetryableCommand, TenantAware {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
}
