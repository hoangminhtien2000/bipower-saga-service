package com.biplus.saga.domain.response;

import com.biplus.core.dto.AbstractBaseDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitmentDecisionResponse extends AbstractBaseDTO {

    private Long processId;
    private Long contractTypeId;
    private Long contractPeriodId;
    private LocalDateTime startWorkDate;
    private Long officialSalary;
    private Long probationarySalary;
    private Long basicSalary;
    private Long negotiableSalary;
    private Long proposedSalary;
    private Long workingPlaceId;
    private Long workingTimeId;
    private String compensationBenefit;
    private String effortReview;
    private String otherIncome;
    private String training;
    private String otherBenefit;
    private String jobDescription;
    private Long contactUserId;
    private Long status;
    private String inchargeHrNote;
    private String hrLeadNote;
    private Boolean candidateResponse;
    private String candidateNote;
    private LocalDateTime estimatedOnboardDate;
    private Boolean onboardStatus;
    private LocalDateTime onboardDate;
    private String rejectReason;

}
