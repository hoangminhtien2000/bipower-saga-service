package com.biplus.saga.domain.command.candidate;

import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.tram.command.CompensatableCommand;
import com.biplus.core.tram.producer.TargetMessage;
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
@TargetMessage("com.biplus.candidate.domain.command.CandidateAssignCommand")
public class CandidateAssignCommand implements CompensatableCommand, Serializable {
    private List<Long> candidateIds;
    private Long inChargeUserId;
    private String note;
    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
}
