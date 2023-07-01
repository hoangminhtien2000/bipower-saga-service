package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.io.Serializable;

@Data
public class EvaluationCriteriaRequest implements Serializable {

    private Long criteriaId;
    private String evaluation;
    private String type;

}
