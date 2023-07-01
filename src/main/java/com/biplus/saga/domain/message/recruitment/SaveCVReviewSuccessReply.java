package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.EmployeeDataResponse;
import com.biplus.saga.domain.response.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.SaveCVReviewSuccessReply")
public class SaveCVReviewSuccessReply implements Serializable {
    private Long candidateId;
    private CandidateResponse candidateResponse;
    private EmployeeDataResponse reviewer;
    private User inchargeUser;
    private String candidateStatus;
    private Boolean isUpdated;
}
