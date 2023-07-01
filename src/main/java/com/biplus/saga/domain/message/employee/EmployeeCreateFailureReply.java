package com.biplus.saga.domain.message.employee;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import com.biplus.saga.domain.dto.UserDTO;
import com.biplus.saga.domain.dto.candidate.Candidate;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@SourceMessage("com.biplus.employee.domain.message.EmployeeCreateFailureReply")
public class EmployeeCreateFailureReply extends FailureMessage {
}
