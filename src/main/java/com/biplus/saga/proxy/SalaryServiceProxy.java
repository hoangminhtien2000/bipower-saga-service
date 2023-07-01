package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.salary.*;
import com.biplus.saga.domain.message.salary.*;
import com.biplus.saga.tramsaga.ServiceChannel;
import com.biplus.saga.tramsaga.state.salary.OvertimeApproveState;
import com.biplus.saga.tramsaga.state.salary.OvertimeConfirmState;
import com.biplus.saga.tramsaga.state.salary.OvertimeCreateState;
import com.biplus.saga.tramsaga.state.salary.OvertimeUpdateState;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class SalaryServiceProxy {
    public final CommandEndpoint<OvertimeCreateCommand> overtimeCreateCommand = CommandEndpointBuilder
            .forCommand(OvertimeCreateCommand.class)
            .withReply(OvertimeCreateSuccessReply.class)
            .withReply(OvertimeCreateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<SalaryRollbackCommand> salaryRollbackCommand = CommandEndpointBuilder
            .forCommand(SalaryRollbackCommand.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<OvertimeUpdateCommand> overtimeUpdateCommand = CommandEndpointBuilder
            .forCommand(OvertimeUpdateCommand.class)
            .withReply(OvertimeUpdateSuccessReply.class)
            .withReply(OvertimeUpdateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<OvertimeApproveCommand> overtimeApproveCommand = CommandEndpointBuilder
            .forCommand(OvertimeApproveCommand.class)
            .withReply(OvertimeApproveSuccessReply.class)
            .withReply(OvertimeApproveFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
    public final CommandEndpoint<OvertimeConfirmCommand> overtimeConfirmCommand = CommandEndpointBuilder
            .forCommand(OvertimeConfirmCommand.class)
            .withReply(OvertimeConfirmSuccessReply.class)
            .withReply(OvertimeConfirmFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
}
