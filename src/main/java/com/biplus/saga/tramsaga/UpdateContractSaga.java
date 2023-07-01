package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.contract.UpdateContractFailureReply;
import com.biplus.saga.domain.message.contract.UpdateContractSuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.proxy.ContractServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.tramsaga.state.UpdateContractState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class UpdateContractSaga implements SimpleSaga<UpdateContractState> {

    private final SagaDefinition<UpdateContractState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public UpdateContractSaga(ContractServiceProxy contractServiceProxy,
                              EmailServiceProxy emailServiceProxy,
                              DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Contract
                step()
                        .invokeParticipant(contractServiceProxy.updateContractCommand, UpdateContractState::makeUpdateLaborContractHistoryCommand)
                        .onReply(UpdateContractSuccessReply.class, UpdateContractState::onUpdateContractSuccess)
                        .onReply(UpdateContractFailureReply.class, UpdateContractState::onUpdateContractFailure)
                        .withCompensation(contractServiceProxy.updateContractRollbackCommand, UpdateContractState::makeUpdateContractRollbackCommand)
                //Email
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, UpdateContractState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, UpdateContractState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, UpdateContractState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, UpdateContractState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.CONTRACT_UPDATE_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[UPDATE_LABOR_CONTRACT_HISTORY_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, UpdateContractState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.CONTRACT_UPDATE_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<UpdateContractState> getSagaDefinition() {
        return sagaDefinition;
    }
}
