package com.biplus.saga.proxy;

import com.biplus.saga.domain.command.email.SendEmailCommand;
import com.biplus.saga.domain.command.email.SendMultiEmailCommand;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.tramsaga.ServiceChannel;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceProxy {

    public final CommandEndpoint<SendEmailCommand> sendEmailCommand = CommandEndpointBuilder
            .forCommand(SendEmailCommand.class)
            .withReply(SendEmailSuccessReply.class)
            .withReply(SendEmailFailureReply.class)
            .withChannel(ServiceChannel.EMAIL_CHANNEL)
            .build();

    public final CommandEndpoint<SendMultiEmailCommand> sendMultiEmailCommand = CommandEndpointBuilder
            .forCommand(SendMultiEmailCommand.class)
            .withReply(SendEmailSuccessReply.class)
            .withReply(SendEmailFailureReply.class)
            .withChannel(ServiceChannel.EMAIL_CHANNEL)
            .build();

}
