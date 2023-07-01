package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.contract.UpdateContractCommand;
import com.biplus.saga.domain.command.contract.UpdateLaborContractHistoryCommand;
import com.biplus.saga.domain.command.contract.ContractRollbackCommand;
import com.biplus.saga.domain.message.contract.UpdateContractFailureReply;
import com.biplus.saga.domain.message.contract.UpdateContractSuccessReply;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistoryFailureReply;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistorySuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContractServiceProxy {

    public final CommandEndpoint<UpdateLaborContractHistoryCommand> updateLaborContractHistoryCommand = CommandEndpointBuilder
            .forCommand(UpdateLaborContractHistoryCommand.class)
            .withReply(UpdateLaborContractHistorySuccessReply.class)
            .withReply(UpdateLaborContractHistoryFailureReply.class)
            .withChannel(ServiceChannel.CONTRACT_CHANNEL)
            .build();

    public final CommandEndpoint<UpdateContractCommand> updateContractCommand = CommandEndpointBuilder
            .forCommand(UpdateContractCommand.class)
            .withReply(UpdateContractSuccessReply.class)
            .withReply(UpdateContractFailureReply.class)
            .withChannel(ServiceChannel.CONTRACT_CHANNEL)
            .build();

    public final CommandEndpoint<ContractRollbackCommand> updateContractRollbackCommand = CommandEndpointBuilder
            .forCommand(ContractRollbackCommand.class)
            .withChannel(ServiceChannel.CONTRACT_CHANNEL)
            .build();

}
