package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.CreateRecruitmentDecisionFailureReply")
public class CreateRecruitmentDecisionFailureReply extends FailureMessage {

}
