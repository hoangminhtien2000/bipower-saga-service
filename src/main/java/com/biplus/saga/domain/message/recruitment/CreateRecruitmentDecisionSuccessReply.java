package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.RecruitmentDecisionResponse;
import com.biplus.saga.domain.response.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@SourceMessage("com.biplus.recruitment.domain.message.CreateRecruitmentDecisionSuccessReply")
public class CreateRecruitmentDecisionSuccessReply implements Serializable {
    private RecruitmentDecisionResponse recruitmentDecision;
    private CandidateResponse candidateResponse;
    private List<User> hrLeaders;
}
