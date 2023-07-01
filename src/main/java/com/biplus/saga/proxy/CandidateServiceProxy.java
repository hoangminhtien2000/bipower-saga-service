package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.candidate.*;
import com.biplus.saga.domain.message.candidate.CandidateAssignFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateAssignSuccessReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusFailureReply;
import com.biplus.saga.domain.message.candidate.CandidateUpdateStatusSuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class CandidateServiceProxy {

    public final CommandEndpoint<CandidateAssignCommand> candidateAssignCommand = CommandEndpointBuilder
            .forCommand(CandidateAssignCommand.class)
            .withReply(CandidateAssignSuccessReply.class)
            .withReply(CandidateAssignFailureReply.class)
            .withChannel(ServiceChannel.CANDIDATE_CHANNEL)
            .build();

    public final CommandEndpoint<CandidateAssignRollbackCommand> candidateAssignRollbackCommand = CommandEndpointBuilder
            .forCommand(CandidateAssignRollbackCommand.class)
            .withChannel(ServiceChannel.CANDIDATE_CHANNEL)
            .build();

    public final CommandEndpoint<CandidateStatusUpdatingCommand> candidateStatusUpdatingCommand = CommandEndpointBuilder
            .forCommand(CandidateStatusUpdatingCommand.class)
            .withReply(CandidateUpdateStatusSuccessReply.class)
            .withReply(CandidateUpdateStatusFailureReply.class)
            .withChannel(ServiceChannel.CANDIDATE_CHANNEL)
            .build();

    public final CommandEndpoint<CandidateStatusUpdatingRollbackCommand> candidateStatusUpdatingRollbackCommand = CommandEndpointBuilder
            .forCommand(CandidateStatusUpdatingRollbackCommand.class)
            .withChannel(ServiceChannel.CANDIDATE_CHANNEL)
            .build();

}
