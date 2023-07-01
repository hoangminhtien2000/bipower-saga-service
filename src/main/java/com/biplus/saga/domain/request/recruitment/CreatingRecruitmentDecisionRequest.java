package com.biplus.saga.domain.request.recruitment;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CreatingRecruitmentDecisionRequest implements Serializable {
    private Long id;
    private Long candidateId;
    private Long contractTypeId;
    private Long contractPeriodId;
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
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
}
