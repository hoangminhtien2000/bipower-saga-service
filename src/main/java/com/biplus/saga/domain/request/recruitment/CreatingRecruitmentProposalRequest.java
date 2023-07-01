package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreatingRecruitmentProposalRequest implements Serializable {
    private Long id;
    private Long candidateId;
    private String netSalary;
    private Long applyPositionId;
    private Long teamId;
    private Double productivity;
    private String project;
    private String hrNote;
}
