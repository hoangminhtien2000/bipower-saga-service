package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.ApprovalRecruitmentProposalSuccessReply")
public class ApprovalRecruitmentProposalSuccessReply implements Serializable {
    private Long candidateId;
    private String candidateName;
    private String inchargeHr;
    private List<String> toEmails;
}
