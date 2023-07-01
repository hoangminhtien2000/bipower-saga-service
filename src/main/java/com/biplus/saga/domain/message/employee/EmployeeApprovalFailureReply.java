package com.biplus.saga.domain.message.employee;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@SourceMessage("com.biplus.employee.domain.message.EmployeeApprovalFailureReply")
public class EmployeeApprovalFailureReply extends FailureMessage {
}
