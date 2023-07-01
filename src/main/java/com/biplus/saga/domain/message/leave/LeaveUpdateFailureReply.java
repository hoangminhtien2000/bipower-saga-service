package com.biplus.saga.domain.message.leave;

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
@SourceMessage("com.biplus.salary.domain.message.LeaveUpdateFailureReply")
public class LeaveUpdateFailureReply extends FailureMessage {
}
