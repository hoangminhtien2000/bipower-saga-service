package com.biplus.saga.domain.request.recruitment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SavingCvReviewRequest {
    @JsonProperty("candidate_id")
    private Long candidateId;
    @JsonProperty("review_status")
    private String reviewStatus;
    @JsonProperty("note")
    private String note;
}
