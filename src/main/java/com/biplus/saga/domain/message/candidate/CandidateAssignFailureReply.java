package com.biplus.saga.domain.message.candidate;


import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@SourceMessage("com.biplus.candidate.domain.message.CandidateAssignFailureReply")
public class CandidateAssignFailureReply extends FailureMessage {
}
