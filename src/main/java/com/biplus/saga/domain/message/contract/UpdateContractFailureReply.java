package com.biplus.saga.domain.message.contract;

import com.biplus.core.message.FailureMessage;
import com.biplus.core.tram.consumer.SourceMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@SourceMessage("com.biplus.contract.domain.message.UpdateContractFailureReply")
public class UpdateContractFailureReply extends FailureMessage {
}
