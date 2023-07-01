package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CandidateSchoolDTO implements Serializable {

    @JsonProperty("candidate_school_id")
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

    @JsonProperty("school_id")
    private Long schoolId;

    @JsonProperty("majors_id")
    private Long majorsId;

    @JsonProperty("graduate_year")
    private LocalDateTime graduateYear;
}
