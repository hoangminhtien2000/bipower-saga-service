package com.biplus.saga.domain.message.email;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@SourceMessage("com.biplus.email.domain.message.SendEmailFailureReply")
public class SendEmailFailureReply extends FailureMessage {
}
