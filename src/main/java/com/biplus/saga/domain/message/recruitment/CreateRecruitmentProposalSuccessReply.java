package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.response.CandidateResponse;
import com.biplus.saga.domain.response.RecruitmentProposalResponse;
import com.biplus.saga.domain.response.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@SourceMessage("com.biplus.recruitment.domain.message.CreateRecruitmentProposalSuccessReply")
public class CreateRecruitmentProposalSuccessReply implements Serializable {
    private Long candidateId;
    private CandidateResponse candidateResponse;
    private RecruitmentProposalResponse proposal;
    private List<User> hrLeaders;
}
