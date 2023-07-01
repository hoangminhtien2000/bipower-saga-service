package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

@Data
public class SaveOnboardStatusRequest {

    private Long id;
    private Boolean candidateResponse;
    private String estimatedOnboardDate;
    private Boolean onboardStatus;
    private String onboardDate;
    private String note;
    private String rejectReason;
    private String rejectOnboardReason;

}
