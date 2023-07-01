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
@TargetMessage("com.biplus.recruitment.domain.command.CreateRecruitmentProposalCommand")
public class CreateRecruitmentProposalCommand implements RetryableCommand, TenantAware {
    private Long id;
    private Long candidateId;
    private String netSalary;
    private Long applyPositionId;
    private Long teamId;
    private Double productivity;
    private String project;
    private String hrNote;
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
}
