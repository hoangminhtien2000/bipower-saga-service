package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class CandidateContactHistoryDTO implements Serializable {

    @JsonProperty("candidate_contact_history_id")
    private Long id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private boolean deleted;
    @JsonProperty("candidate_id")
    private Long candidateId;
    @JsonProperty("contact_id")
    private Long contactId;
    @JsonProperty("contact_status_id")
    private Long contactStatusId;
    @JsonProperty("status_after_contact_id")
    private Long statusAfterContactId;
    @JsonProperty("response_id")
    private Long responseId;
    private String note;
    @JsonProperty("candidate_status_id")
    private Long candidateStatusId;
    @JsonProperty("contact_time")
    private LocalDateTime contactTime;
    private Long contactUserId;

    @JsonProperty("candidate_status")
    private ItemDTO candidateStatus;

    private ItemDTO response;

    @JsonProperty("status_after_contact")
    private ItemDTO statusAfterContact;

    @JsonProperty("contact_status")
    private ItemDTO contactStatus;

    @JsonProperty("contact_user")
    private UserDTO contactUser;

    private ItemDTO contact;

}
