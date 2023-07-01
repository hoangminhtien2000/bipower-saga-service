package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.InterviewEvaluationSuccessReply")
public class InterviewEvaluationSuccessReply implements Serializable {
    private boolean updateCandidateStatus;
    private boolean passInterview;
}
