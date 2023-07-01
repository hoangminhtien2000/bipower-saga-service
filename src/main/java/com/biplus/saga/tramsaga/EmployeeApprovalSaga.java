package com.biplus.saga.tramsaga;

import com.biplus.core.tram.saga.SagaAspectProxyFactory;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.event.SagaCompletedEvent;
import com.biplus.saga.domain.event.SagaRollbackEvent;
import com.biplus.saga.domain.message.email.SendEmailFailureReply;
import com.biplus.saga.domain.message.email.SendEmailSuccessReply;
import com.biplus.saga.domain.message.employee.EmployeeApprovalFailureReply;
import com.biplus.saga.domain.message.employee.EmployeeApprovalSuccessReply;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeFailureReply;
import com.biplus.saga.domain.message.team.TeamAssignRoleAndTeamForEmployeeSuccessReply;
import com.biplus.saga.proxy.EmailServiceProxy;
import com.biplus.saga.proxy.EmployeeServiceProxy;
import com.biplus.saga.proxy.TeamServiceProxy;
import com.biplus.saga.tramsaga.state.employee.EmployeeApprovalState;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import org.springframework.stereotype.Component;

import static com.biplus.saga.tramsaga.ServiceChannel.COMPLETE_SAGA_CHANNEL;
import static java.util.Collections.singletonList;

@Component
public class EmployeeApprovalSaga implements SimpleSaga<EmployeeApprovalState> {

    private final SagaDefinition<EmployeeApprovalState> sagaDefinition;
    private final DomainEventPublisher domainEventPublisher;

    public EmployeeApprovalSaga(EmployeeServiceProxy employeeServiceProxy,
                                EmailServiceProxy emailServiceProxy,
                                TeamServiceProxy teamServiceProxy,
                                DomainEventPublisher domainEventPublisher) {
        sagaDefinition = SagaAspectProxyFactory.getProxy(
                //Employee
                step()
                        .invokeParticipant(employeeServiceProxy.employeeApprovalCommand, EmployeeApprovalState::makeCommand)
                        .onReply(EmployeeApprovalSuccessReply.class, EmployeeApprovalState::onSuccess)
                        .onReply(EmployeeApprovalFailureReply.class, EmployeeApprovalState::onFailure)
                        .withCompensation(employeeServiceProxy.employeeApprovalRollbackCommand, EmployeeApprovalState::makeRollbackCommand)
                //team
                .step()
                        .invokeParticipant(teamServiceProxy.teamAssignRoleAndTeamForEmployeeCommand, EmployeeApprovalState::makeAssignTeamAndRoleCommand)
                        .onReply(TeamAssignRoleAndTeamForEmployeeSuccessReply.class, EmployeeApprovalState::onAssignTeamAndRoleSuccess)
                        .onReply(TeamAssignRoleAndTeamForEmployeeFailureReply.class, EmployeeApprovalState::onAssignTeamAndRoleFailure)
                        .withCompensation(teamServiceProxy.teamRollbackCommand, EmployeeApprovalState::makeAssignTeamAndRoleRollbackCommand)
                        //Email
                .step()
                        .invokeParticipant(emailServiceProxy.sendEmailCommand, EmployeeApprovalState::makeSendEmailCommand)
                        .onReply(SendEmailSuccessReply.class, EmployeeApprovalState::onSendEmailSuccess)
                        .onReply(SendEmailFailureReply.class, EmployeeApprovalState::onSendEmailFailure)

                        .build());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void onSagaCompletedSuccessfully(String sagaId, EmployeeApprovalState state) {
        SagaCompletedEvent completedEvent = new SagaCompletedEvent();
        completedEvent.setActionType(ActionType.EMPLOYEE_APPROVAL_CHANNEL);
        completedEvent.setSagaId(sagaId);
        completedEvent.setSuccessMessage("[EMPLOYEE_APPROVAL_CHANNEL] successfully!");
        completedEvent.setSuccess(true);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId, singletonList(completedEvent));
    }

    @Override
    public void onSagaRolledBack(String sagaId, EmployeeApprovalState state) {
        SagaRollbackEvent rollbackEvent = new SagaRollbackEvent(ActionType.EMPLOYEE_APPROVAL_CHANNEL, state.getErrorCode(), state.getErrorMessage());
        rollbackEvent.setSuccess(false);
        domainEventPublisher.publish(COMPLETE_SAGA_CHANNEL, sagaId,
                singletonList(rollbackEvent));
    }

    @Override
    public SagaDefinition<EmployeeApprovalState> getSagaDefinition() {
        return sagaDefinition;
    }
}
