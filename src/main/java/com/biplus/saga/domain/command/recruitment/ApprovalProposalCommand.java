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
@TargetMessage("com.biplus.recruitment.domain.command.ApprovalProposalCommand")
public class ApprovalProposalCommand implements RetryableCommand, TenantAware {

    private Long id;
    private String note;
    private Boolean approval;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;



}
