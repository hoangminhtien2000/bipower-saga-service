package com.biplus.saga.domain.message.employee;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@SourceMessage("com.biplus.employee.domain.message.UpdateLaborContractTypeFailureReply")
public class UpdateLaborContractTypeFailureReply extends FailureMessage {
}
