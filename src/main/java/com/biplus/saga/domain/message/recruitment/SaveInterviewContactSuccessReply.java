package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.SaveInterviewContactSuccessReply")
public class SaveInterviewContactSuccessReply implements Serializable {
}
