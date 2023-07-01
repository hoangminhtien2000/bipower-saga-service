package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveInterviewResultRequest implements Serializable {

    private Long id;
    private Long candidateId;
    private String type;
    private Long levelId;
    private String workAbility;
    private String jobSuitability;
    private String teamworkAbility;
    private String longTermCommitment;
    private Long salary;
    private Boolean decision;
    private List<EvaluationCriteriaRequest> evaluationCriterias;

}
