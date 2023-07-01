package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.SaveCVReviewFailureReply")
public class SaveCVReviewFailureReply extends FailureMessage {
    private boolean success = false;
}
