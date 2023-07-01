package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CandidateDomainDTO implements Serializable {

    @JsonProperty("candidate_domain_id")
    private Long id;

    private String createdBy;

    @JsonProperty("create_time")
    private LocalDateTime createdAt;

    private String lastModifiedBy;

    @JsonProperty("last_update_time")
    private LocalDateTime lastModifiedAt;

    private boolean deleted;

    @JsonProperty("candidate_id")
    private Long candidateId;

    @JsonProperty("domain_id")
    private Long domainId;
}
