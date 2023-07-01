package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.leave.LeaveConfirmCommand;
import com.biplus.saga.domain.command.leave.LeaveApproveCommand;
import com.biplus.saga.domain.command.leave.LeaveCancelCommand;
import com.biplus.saga.domain.command.leave.LeaveCreateCommand;
import com.biplus.saga.domain.command.leave.LeaveUpdateCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.message.leave.*;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class LeaveServiceProxy {
    public final CommandEndpoint<LeaveCreateCommand> leaveCreateCommand = CommandEndpointBuilder
            .forCommand(LeaveCreateCommand.class)
            .withReply(LeaveCreateSuccessReply.class)
            .withReply(LeaveCreateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<SalaryRollbackCommand> leaveRollbackCommand = CommandEndpointBuilder
            .forCommand(SalaryRollbackCommand.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<LeaveUpdateCommand> leaveUpdateCommand = CommandEndpointBuilder
            .forCommand(LeaveUpdateCommand.class)
            .withReply(LeaveUpdateSuccessReply.class)
            .withReply(LeaveUpdateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<LeaveApproveCommand> leaveApproveCommand = CommandEndpointBuilder
            .forCommand(LeaveApproveCommand.class)
            .withReply(LeaveApproveSuccessReply.class)
            .withReply(LeaveApproveFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<LeaveCancelCommand> leaveCancelCommand = CommandEndpointBuilder
            .forCommand(LeaveCancelCommand.class)
            .withReply(LeaveCancelSuccessReply.class)
            .withReply(LeaveCancelFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<LeaveConfirmCommand> leaveConfirmCommand = CommandEndpointBuilder
            .forCommand(LeaveConfirmCommand.class)
            .withReply(LeaveConfirmSuccessReply.class)
            .withReply(LeaveConfirmFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
}
