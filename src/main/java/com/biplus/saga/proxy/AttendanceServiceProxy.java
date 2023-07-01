package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.attendance.SendRequestEditWorkingTimeCommand;
import com.biplus.saga.domain.command.attendance.ConfirmManyRequestEditWorkingTimeCommand;
import com.biplus.saga.domain.command.attendance.WorkOutsideCreateCommand;
import com.biplus.saga.domain.command.attendance.WorkOutsideUpdateCommand;
import com.biplus.saga.domain.command.attendance.SendRequestWorkOutsideCommand;
import com.biplus.saga.domain.command.attendance.ConfirmWorkOutsideCommand;
import com.biplus.saga.domain.command.attendance.WorkingTimeCreateCommand;
import com.biplus.saga.domain.command.attendance.WorkingTimeUpdateCommand;
import com.biplus.saga.domain.command.salary.SalaryRollbackCommand;
import com.biplus.saga.domain.message.attendance.SendRequestEditWorkingTimeFailureReply;
import com.biplus.saga.domain.message.attendance.SendRequestEditWorkingTimeSuccessReply;
import com.biplus.saga.domain.message.attendance.ConfirmManyRequestEditWorkingTimeFailureReply;
import com.biplus.saga.domain.message.attendance.ConfirmManyRequestEditWorkingTimeSuccessReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideCreateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideCreateSuccessReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkOutsideUpdateSuccessReply;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.SendRequestWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideFailureReply;
import com.biplus.saga.domain.message.attendance.ConfirmWorkOutsideSuccessReply;
import com.biplus.saga.domain.message.attendance.WorkingTimeUpdateFailureReply;
import com.biplus.saga.domain.message.attendance.WorkingTimeUpdateSuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class AttendanceServiceProxy {
    public final CommandEndpoint<WorkingTimeCreateCommand> workingTimeCreateCommand = CommandEndpointBuilder
            .forCommand(WorkingTimeCreateCommand.class)
            .withReply(WorkingTimeUpdateSuccessReply.class)
            .withReply(WorkingTimeUpdateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<WorkingTimeUpdateCommand> workingTimeUpdateCommand = CommandEndpointBuilder
            .forCommand(WorkingTimeUpdateCommand.class)
            .withReply(WorkingTimeUpdateSuccessReply.class)
            .withReply(WorkingTimeUpdateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<SendRequestEditWorkingTimeCommand> sendRequestEditWorkingTimeCommand = CommandEndpointBuilder
            .forCommand(SendRequestEditWorkingTimeCommand.class)
            .withReply(SendRequestEditWorkingTimeSuccessReply.class)
            .withReply(SendRequestEditWorkingTimeFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<ConfirmManyRequestEditWorkingTimeCommand> confirmManyRequestEditWorkingTimeCommand =
            CommandEndpointBuilder
                    .forCommand(ConfirmManyRequestEditWorkingTimeCommand.class)
                    .withReply(ConfirmManyRequestEditWorkingTimeSuccessReply.class)
                    .withReply(ConfirmManyRequestEditWorkingTimeFailureReply.class)
                    .withChannel(ServiceChannel.SALARY_CHANNEL)
                    .build();

    public final CommandEndpoint<WorkOutsideCreateCommand> createWorkOutsideCommand =
            CommandEndpointBuilder
                    .forCommand(WorkOutsideCreateCommand.class)
                    .withReply(WorkOutsideCreateSuccessReply.class)
                    .withReply(WorkOutsideCreateFailureReply.class)
                    .withChannel(ServiceChannel.SALARY_CHANNEL)
                    .build();

    public final CommandEndpoint<WorkOutsideUpdateCommand> workOutsideUpdateCommand = CommandEndpointBuilder
            .forCommand(WorkOutsideUpdateCommand.class)
            .withReply(WorkOutsideUpdateSuccessReply.class)
            .withReply(WorkOutsideUpdateFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<SendRequestWorkOutsideCommand> sendRequestWorkOutsideCommand = CommandEndpointBuilder
            .forCommand(SendRequestWorkOutsideCommand.class)
            .withReply(SendRequestWorkOutsideSuccessReply.class)
            .withReply(SendRequestWorkOutsideFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<ConfirmWorkOutsideCommand> confirmWorkOutsideCommand = CommandEndpointBuilder
            .forCommand(ConfirmWorkOutsideCommand.class)
            .withReply(ConfirmWorkOutsideSuccessReply.class)
            .withReply(ConfirmWorkOutsideFailureReply.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();

    public final CommandEndpoint<SalaryRollbackCommand> attendanceRollbackCommand = CommandEndpointBuilder
            .forCommand(SalaryRollbackCommand.class)
            .withChannel(ServiceChannel.SALARY_CHANNEL)
            .build();
}
