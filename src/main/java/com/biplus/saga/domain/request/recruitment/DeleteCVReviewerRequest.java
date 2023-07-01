package com.biplus.saga.domain.request.recruitment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteCVReviewerRequest {
    @JsonProperty("id")
    private Long id;
}
