package com.biplus.saga.domain.command.recruitment;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.RetryableCommand;
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
@TargetMessage("com.biplus.recruitment.domain.command.SaveOnboardStatusCommand")
public class SaveOnboardStatusCommand implements RetryableCommand, TenantAware {

    private Long id;
    private Boolean candidateResponse;
    private LocalDateTime estimatedOnboardDate;
    private Boolean onboardStatus;
    private LocalDateTime onboardDate;
    private String note;
    private String rejectReason;
    private String rejectOnboardReason;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;

}
