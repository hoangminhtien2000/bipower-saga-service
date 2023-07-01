package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.EmployeeDataResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@SourceMessage("com.biplus.recruitment.domain.message.AssignCVReviewerSuccessReply")
public class AssignCVReviewerSuccessReply implements Serializable {
    private Long candidateId;
    private CandidateResponse candidateResponse;
    private List<EmployeeDataResponse> reviewerInfos;
    private String candidateStatus;
    private Boolean isUpdated;
}
