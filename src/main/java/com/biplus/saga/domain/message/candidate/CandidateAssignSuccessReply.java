package com.biplus.saga.domain.message.candidate;

import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.UserDTO;
import com.biplus.saga.domain.dto.candidate.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SourceMessage("com.biplus.candidate.domain.message.CandidateAssignSuccessReply")
public class CandidateAssignSuccessReply implements Serializable {
    List<Candidate> candidateList;
    UserDTO assigner;
}
