package com.biplus.saga.domain.message.candidate;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.*;

@Data
@SourceMessage("com.biplus.candidate.domain.message.CandidateUpdateStatusFailureReply")
public class CandidateUpdateStatusFailureReply extends FailureMessage {
}
