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
@SourceMessage("com.biplus.recruitment.domain.message.ApprovalRecruitmentDecisionSuccessReply")
public class ApprovalRecruitmentDecisionSuccessReply implements Serializable {
    private Long candidateId;
    private String candidateName;
    private String hrLeadName;
    private String candidateLevel;
    private String candidatePosition;
    private List<String> toEmails;
}
