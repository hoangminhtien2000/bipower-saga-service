package com.biplus.saga.domain.message.recruitment;

import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@SourceMessage("com.biplus.recruitment.domain.message.DeleteCVReviewerSuccessReply")
public class DeleteCVReviewerSuccessReply implements Serializable {
    private Long candidateId;
    private String candidateStatus;
    private Boolean isUpdated;
}
