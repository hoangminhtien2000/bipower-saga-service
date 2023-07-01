package com.biplus.saga.domain.command.candidate;

import com.biplus.core.tram.command.RetryableCommand;
import com.biplus.core.tram.command.TenantAware;
import com.biplus.core.tram.producer.TargetMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Builder
@TargetMessage("com.biplus.candidate.domain.command.CandidateStatusUpdatingRollbackCommand")
public class CandidateStatusUpdatingRollbackCommand implements RetryableCommand, TenantAware {
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
}
