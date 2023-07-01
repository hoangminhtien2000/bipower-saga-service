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
@TargetMessage("com.biplus.recruitment.domain.command.RecruitmentDecisionCreateCommand")
public class RecruitmentDecisionCreateCommand implements RetryableCommand, TenantAware {
    private Long id;
    private Long candidateId;
    private Long contractTypeId;
    private Long contractPeriodId;
    private LocalDateTime startWorkDate;
    private String officialSalary;
    private String probationarySalary;
    private String basicSalary;
    private String negotiableSalary;
    private String proposedSalary;
    private Long workingPlaceId;
    private Long workingTimeId;
    private String compensationBenefit;
    private String effortReview;
    private String otherIncome;
    private String training;
    private String otherBenefit;
    private String jobDescription;
    private Long contactUserId;
    private Boolean status;
    private String inchargeHrNote;

    private Locale locale;
    private LocalDateTime sysDate;
    private String companyCid;
    private ActionUserDTO actionUserDTO;
}
