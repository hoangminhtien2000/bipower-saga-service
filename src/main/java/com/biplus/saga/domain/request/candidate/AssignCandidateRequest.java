package com.biplus.saga.domain.request.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AssignCandidateRequest {

    @JsonProperty("candidate_ids")
    private List<Long> candidateIds;

    @JsonProperty("in_charge_user_id")
    private Long inChargeUserId;

    private String note;



}
