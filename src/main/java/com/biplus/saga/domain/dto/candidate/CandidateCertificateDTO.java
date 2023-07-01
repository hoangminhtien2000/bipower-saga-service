package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CandidateCertificateDTO implements Serializable {
    @JsonProperty("candidate_certificate_id")
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private boolean deleted;
    @JsonProperty("candidate_id")
    private Long candidateId;
    @JsonProperty("certificate_id")
    private Long certificateId;
}
