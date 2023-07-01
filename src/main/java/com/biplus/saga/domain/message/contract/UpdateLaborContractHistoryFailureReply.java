package com.biplus.saga.domain.message.contract;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@SourceMessage("com.biplus.contract.domain.message.UpdateLaborContractHistoryFailureReply")
public class UpdateLaborContractHistoryFailureReply extends FailureMessage {
}
