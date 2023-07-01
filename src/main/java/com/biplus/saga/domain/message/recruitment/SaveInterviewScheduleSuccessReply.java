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
@SourceMessage("com.biplus.recruitment.domain.message.SaveInterviewScheduleSuccessReply")
public class SaveInterviewScheduleSuccessReply implements Serializable {
    private boolean updateCandidateStatus;
    private String candidateName;
    private String interviewType;
    private String candidateLevel;
    private String candidatePosition;
    private List<String> toEmails;
}
