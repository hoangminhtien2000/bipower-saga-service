package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApprovalRecruitmentDecisionRequest implements Serializable {

    private Long id;
    private String note;
    private Boolean approval;

}
