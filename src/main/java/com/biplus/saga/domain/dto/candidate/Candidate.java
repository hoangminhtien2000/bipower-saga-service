package com.biplus.saga.domain.dto.candidate;

import com.biplus.core.entity.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    private static final long serialVersionUID = 4590066832132670826L;

    private String candidateCode;

    private String fullName;

    private LocalDateTime birthDate;

    private String phone;

    private String email;

    private String address;

    private String gender;

    private Long sourceId;

    private LocalDateTime receiveTime;

    private String jdLink;

    private Long applyPositionId;

    private Long levelId;

    private String oldCompany;

    private LocalDateTime startWorkTime;

    private Long technologyId;

    private LocalDateTime startTechnologyTime;

    private Long maxLiteracyId;

    private Long literacyEnglishId;

    private Long inChargeUserId;

    private Long statusId;

    private Long createUserId;

    protected Long id;

    private String createdBy;

    private LocalDateTime createdAt;

    private String lastModifiedBy;

    private boolean deleted;
}
