package com.biplus.saga.domain.message.email;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.email.domain.message.SendEmailSuccessReply")
public class SendEmailSuccessReply implements Serializable {
}
