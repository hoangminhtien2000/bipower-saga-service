package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.SaveOnboardStatusFailureReply")
public class SaveOnboardStatusFailureReply extends FailureMessage {
}
