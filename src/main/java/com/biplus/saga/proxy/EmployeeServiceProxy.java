package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.employee.EmployeeApprovalCommand;
import com.biplus.saga.domain.command.employee.EmployeeCreateCommand;
import com.biplus.saga.domain.command.employee.EmployeeRollbackCommand;
import com.biplus.saga.domain.command.employee.UpdateLaborContractTypeCommand;
import com.biplus.saga.domain.message.employee.EmployeeApprovalFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeApprovalSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeCreateSuccessReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeFailureReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeSuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeServiceProxy {

    public final CommandEndpoint<UpdateLaborContractTypeCommand> updateLaborContractTypeCommand = CommandEndpointBuilder
            .forCommand(UpdateLaborContractTypeCommand.class)
            .withReply(UpdateLaborContractTypeSuccessReply.class)
            .withReply(UpdateLaborContractTypeFailureReply.class)
            .withChannel(ServiceChannel.EMPLOYEE_CHANNEL)
            .build();
    public final CommandEndpoint<EmployeeCreateCommand> employeeCreateCommand = CommandEndpointBuilder
            .forCommand(EmployeeCreateCommand.class)
            .withReply(EmployeeCreateSuccessReply.class)
            .withReply(EmployeeCreateFailureReply.class)
            .withChannel(ServiceChannel.EMPLOYEE_CHANNEL)
            .build();
    public final CommandEndpoint<EmployeeRollbackCommand> employeeCreateRollbackCommand = CommandEndpointBuilder
            .forCommand(EmployeeRollbackCommand.class)
            .withChannel(ServiceChannel.EMPLOYEE_CHANNEL)
            .build();

    public final CommandEndpoint<EmployeeApprovalCommand> employeeApprovalCommand = CommandEndpointBuilder
            .forCommand(EmployeeApprovalCommand.class)
            .withReply(EmployeeApprovalSuccessReply.class)
            .withReply(EmployeeApprovalFailureReply.class)
            .withChannel(ServiceChannel.EMPLOYEE_CHANNEL)
            .build();
    public final CommandEndpoint<EmployeeRollbackCommand> employeeApprovalRollbackCommand = CommandEndpointBuilder
            .forCommand(EmployeeRollbackCommand.class)
            .withChannel(ServiceChannel.EMPLOYEE_CHANNEL)
            .build();

}
