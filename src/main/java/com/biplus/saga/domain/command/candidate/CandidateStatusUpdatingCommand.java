package com.biplus.saga.domain.command.candidate;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.RetryableCommand;
import com.biplus.core.tram.command.TenantAware;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Locale;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TargetMessage("com.biplus.candidate.domain.command.CandidateStatusUpdatingCommand")
public class CandidateStatusUpdatingCommand implements RetryableCommand, TenantAware {

    private Long candidateId;
    private String candidateStatus;
    private LocalDateTime estimateOnboardDate;
    private Boolean isUpdated;
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
}
