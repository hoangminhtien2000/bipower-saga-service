package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.leave.LeaveApproveCommand;
import com.biplus.saga.domain.command.leave.LeaveCancelCommand;
import com.biplus.saga.domain.command.leave.LeaveConfirmCommand;
import com.biplus.saga.domain.command.leave.LeaveCreateCommand;
import com.biplus.saga.domain.command.leave.LeaveUpdateCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.command.team.TeamAssignRoleAndTeamForEmployeeCommand;
import com.biplus.saga.domain.command.team.TeamRollbackCommand;
import com.biplus.saga.domain.message.leave.*;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeFailureReply;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeSuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class TeamServiceProxy {
    public final CommandEndpoint<TeamAssignRoleAndTeamForEmployeeCommand> teamAssignRoleAndTeamForEmployeeCommand = CommandEndpointBuilder
            .forCommand(TeamAssignRoleAndTeamForEmployeeCommand.class)
            .withReply(TeamAssignRoleAndTeamForEmployeeSuccessReply.class)
            .withReply(TeamAssignRoleAndTeamForEmployeeFailureReply.class)
            .withChannel(ServiceChannel.TEAM_CHANNEL)
            .build();
    public final CommandEndpoint<TeamRollbackCommand> teamRollbackCommand = CommandEndpointBuilder
            .forCommand(TeamRollbackCommand.class)
            .withChannel(ServiceChannel.TEAM_CHANNEL)
            .build();

}
