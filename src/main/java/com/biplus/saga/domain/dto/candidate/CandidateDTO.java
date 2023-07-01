package com.biplus.saga.domain.dto.candidate;

import com.biplus.core.dto.AbstractBaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateDTO extends AbstractBaseDTO {

    @JsonProperty("candidate_id")
    private Long id;

    @JsonProperty("candidate_code")
    private String candidateCode;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("old_company")
    private String oldCompany;

    @JsonProperty("start_work_time")
    private LocalDateTime startWorkTime;

    @JsonProperty("start_technology_time")
    private LocalDateTime startTechnologyTime;

    @JsonProperty("max_literacy_id")
    private Long maxLiteracyId;

    @JsonProperty("literacy_english_id")
    private Long literacyEnglishId;

    @JsonProperty("apply_position_id")
    private Long applyPositionId;

    @JsonProperty("technology_id")
    private Long technologyId;

    @JsonProperty("in_charge_user_id")
    private Long inChargeUserId;

    @JsonProperty("apply_position")
    private ItemDTO applyPosition;

    @JsonProperty("technology")
    private ItemDTO technology;

    private ItemDTO status;

    @JsonProperty("max_literacy")
    private ItemDTO maxLiteracy;

    @JsonProperty("literacy_english")
    private ItemDTO literacyEnglish;

    @JsonProperty("in_charge_user")
    private UserDTO inChargeUser;

    @JsonProperty("create_user")
    private UserDTO createUser;

    @JsonProperty("source")
    private ItemDTO source;

    @JsonProperty("level")
    private ItemDTO level;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    private String address;

    @JsonProperty("source_id")
    private Long sourceId;

    @JsonProperty("can_view_detail")
    private boolean canViewDetail;

    @JsonProperty("display")
    private String display;

    @JsonProperty("jd_link")
    private String jdLink;

    @JsonProperty("status_id")
    private Long statusId;

    @JsonProperty("level_id")
    private Long levelId;

    @JsonProperty("receive_time")
    private LocalDateTime receiveTime;

    @JsonProperty("certificates")
    private List<CandidateCertificateDTO> certificates;

    @JsonProperty("contacts")
    private List<CandidateContactDTO> contacts;

    @JsonProperty("domains")
    private List<CandidateDomainDTO> domains;

    @JsonProperty("schools")
    private List<CandidateSchoolDTO> schools;

    @JsonProperty("contact_histories")
    private List<CandidateContactHistoryDTO> contactHistories;

    @JsonProperty("files")
    private List<CandidateFileDTO> files;

    @JsonProperty("create_time")
    @Override
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }
}
