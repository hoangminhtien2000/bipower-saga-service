package com.biplus.saga.domain.response;

import com.biplus.core.dto.AbstractBaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CandidateResponse extends AbstractBaseDTO {

    private Long id;

    private String candidateCode;

    private String fullName;

    private LocalDateTime birthDate;

    private String gender;

    private String oldCompany;

    private LocalDateTime startWorkTime;

    private LocalDateTime startTechnologyTime;

    private Long maxLiteracyId;

    private Long literacyEnglishId;

    private Long applyPositionId;

    private Long technologyId;

    private Long inChargeUserId;

    private ItemResponse applyPosition;

    private ItemResponse technology;

    private ItemResponse status;

    private ItemResponse maxLiteracy;

    private ItemResponse literacyEnglish;

    private ItemResponse source;

    private ItemResponse level;

    private String phone;

    private String email;

    private String address;

    private Long sourceId;

    private boolean canViewDetail;

    private String display;

    private String jdLink;

    private Long statusId;

    private Long levelId;

    private LocalDateTime receiveTime;
}
