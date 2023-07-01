package com.biplus.saga.domain.message.candidate;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.candidate.CandidateDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@SourceMessage("com.biplus.candidate.domain.message.CandidateUpdateStatusSuccessReply")
public class CandidateUpdateStatusSuccessReply implements Serializable {
    private Long candidateId;
    private Long candidateStatusId;
    private CandidateDTO candidateDTO;
}
