package com.biplus.saga.domain.request.recruitment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AssignCVReviewerRequest {
    @JsonProperty("candidate_id")
    private Long candidateId;
    @JsonProperty("reviewers")
    private List<Long> reviewers;
    @JsonProperty("note")
    private String note;
}
