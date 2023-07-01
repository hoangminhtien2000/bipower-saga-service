package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistoryFailureReply;
import com.biplus.saga.domain.message.contract.UpdateLaborContractHistorySuccessReply;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeFailureReply;
import com.biplus.saga.domain.message.employee.UpdateLaborContractTypeSuccessReply;
import com.biplus.saga.proxy.ContractServiceProxy;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.EmployeeServiceProxy;
import com.biplus.saga.tramsaga.state.UpdateLaborContractHistoryState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class UpdateLaborContractHistorySaga implements SimpleSaga<UpdateLaborContractHistoryState> {

    private final SagaDefinition<UpdateLaborContractHistoryState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public UpdateLaborContractHistorySaga(ContractServiceProxy contractServiceProxy,
                                          EmployeeServiceProxy employeeServiceProxy,
                                          EmailServiceProxy emailServiceProxy,
                                          DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Contract
                step()
                        .invokeParticipant(contractServiceProxy.updateLaborContractHistoryCommand, UpdateLaborContractHistoryState::makeUpdateLaborContractHistoryCommand)
                        .onReply(UpdateLaborContractHistorySuccessReply.class, UpdateLaborContractHistoryState::onUpdateLaborContractHistorySuccess)
                        .onReply(UpdateLaborContractHistoryFailureReply.class, UpdateLaborContractHistoryState::onUpdateLaborContractHistoryFailure)
                        .withCompensation(contractServiceProxy.updateContractRollbackCommand, UpdateLaborContractHistoryState::makeUpdateLaborContractHistoryRollbackCommand)
                //Employee
                .step()
                        .invokeParticipant(employeeServiceProxy.updateLaborContractTypeCommand, UpdateLaborContractHistoryState::makeUpdateLaborContractTypeCommand)
                        .onReply(UpdateLaborContractTypeSuccessReply.class, UpdateLaborContractHistoryState::onUpdateLaborContractTypeSuccess)
                        .onReply(UpdateLaborContractTypeFailureReply.class, UpdateLaborContractHistoryState::onUpdateLaborContractTypeFailure)
                        .withCompensation(employeeServiceProxy.employeeCreateRollbackCommand, UpdateLaborContractHistoryState::makeUpdateEmployeeRollbackCommand)
                //Email
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, UpdateLaborContractHistoryState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, UpdateLaborContractHistoryState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, UpdateLaborContractHistoryState::onSendEmailFailure)

                        .build());

        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, UpdateLaborContractHistoryState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.UPDATE_LABOR_CONTRACT_HISTORY_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[UPDATE_LABOR_CONTRACT_HISTORY_CHANNEL] successfully!");
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, UpdateLaborContractHistoryState state) {
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(new SagaRollbackEvent(ActionType.UPDATE_LABOR_CONTRACT_HISTORY_CHANNEL, state.getErrorCode(), state.getErrorMessage())));
    }

    @Override
    public SagaDefinition<UpdateLaborContractHistoryState> getSagaDefinition() {
        return sagaDefinition;
    }
}
