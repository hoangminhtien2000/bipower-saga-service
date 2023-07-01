package com.biplus.saga.configuration;

import com.biplus.saga.handler.SagaEventHandler;
import com.biplus.saga.tramsaga.*;
import com.biplus.saga.tramsaga.state.*;
import com.biplus.saga.tramsaga.state.attendance.ConfirmManyRequestEditWorkingTimeState;
import com.biplus.saga.tramsaga.state.attendance.ConfirmWorkOutsideState;
import com.biplus.saga.tramsaga.state.attendance.SendRequestEditWorkingTimeState;
import com.biplus.saga.tramsaga.state.attendance.SendRequestWorkOutsideState;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideCreateState;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideUpdateState;
import com.biplus.saga.tramsaga.state.attendance.WorkingTimeCreateState;
import com.biplus.saga.tramsaga.state.attendance.WorkingTimeUpdateState;
import com.biplus.saga.tramsaga.state.CandidateAssignState;
import com.biplus.saga.tramsaga.state.recruitment.DeleteCVReviewerState;
import com.biplus.saga.tramsaga.state.UpdateContractState;
import com.biplus.saga.tramsaga.state.UpdateLaborContractHistoryState;
import com.biplus.saga.tramsaga.state.employee.EmployeeApprovalState;
import com.biplus.saga.tramsaga.state.employee.EmployeeCreateState;
import com.biplus.saga.tramsaga.state.leave.*;
import com.biplus.saga.tramsaga.state.recruitment.*;
import com.biplus.saga.tramsaga.state.salary.OvertimeApproveState;
import com.biplus.saga.tramsaga.state.salary.OvertimeConfirmState;
import com.biplus.saga.tramsaga.state.salary.OvertimeCreateState;
import com.biplus.saga.tramsaga.state.salary.OvertimeUpdateState;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TramsConfiguration {
    @Bean
    public DomainEventDispatcher completeEventDispatcher(SagaEventHandler sagaEventHandler, MessageConsumer messageConsumer) {
        return new DomainEventDispatcher("sagaEvents" + UUID.randomUUID().toString(), sagaEventHandler.domainEventHandlers(), messageConsumer);
    }

    @Bean
    public SagaManager<UpdateLaborContractHistoryState> updateLaborContractHistoryManager(UpdateLaborContractHistorySaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<CandidateAssignState> candidateAssignStateSagaManager(CandidateAssignSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<EmployeeCreateState> employeeCreateSagaManager(EmployeeCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<EmployeeApprovalState> employeeApprovalSagaManager(EmployeeApprovalSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<OvertimeCreateState> overtimeCreateSagaManager(OvertimeCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<UpdateContractState> updateContractManager(UpdateContractSaga saga) {
        return new SagaManagerImpl<>(saga);
    }
    @Bean
    public SagaManager<OvertimeUpdateState> updateOvertimeManager(OvertimeUpdateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<OvertimeApproveState> approveOvertimeManager(OvertimeApproveSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<OvertimeConfirmState> confirmOvertimeManager(OvertimeConfirmSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<LeaveCreateState> createLeaveManager(LeaveCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<LeaveUpdateState> updateLeaveManager(LeaveUpdateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<LeaveApproveState> approveLeaveManager(LeaveApproveSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<LeaveCancelState> cancelLeaveManager(LeaveCancelSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<LeaveConfirmState> confirmLeaveManager(LeaveConfirmSaga saga) {
        return new SagaManagerImpl<>(saga);
    }
    @Bean
    public SagaManager<CVAssignReviewerState> assignCVReviewerStateSagaManager(CVAssignReviewerSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<DeleteCVReviewerState> deleteCVReviewerStateSagaManager(DeleteCVReviewerSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SavingCVReviewState> saveCVReviewStateSagaManager(SavingCVReviewSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SaveInterviewContactState> saveInterviewContactStateSagaManager(SaveInterviewContactSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SaveInterviewScheduleState> saveInterviewScheduleStateSagaManager(SaveInterviewScheduleSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<InterviewEvaluationState> interviewEvaluationSagaManager(InterviewEvaluationSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<WorkingTimeCreateState> createWorkingTimeManager(WorkingTimeCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<WorkingTimeUpdateState> updateWorkingTimeManager(WorkingTimeUpdateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SendRequestEditWorkingTimeState> sendRequestEditWorkingTimeManager(SendRequestEditWorkingTimeSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<ConfirmManyRequestEditWorkingTimeState> confirmManyRequestEditWorkingTimeManager(ConfirmManyRequestEditWorkingTimeSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<WorkOutsideCreateState> createWorkOutsideManager(WorkOutsideCreateStateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<WorkOutsideUpdateState> updateWorkOutsideManager(WorkOutsideUpdateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SendRequestWorkOutsideState> sendRequestWorkOutsideManager(SendRequestWorkOutsideSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<ConfirmWorkOutsideState> confirmWorkOutsideManager(ConfirmWorkOutsideSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<RecruitmentProposalCreateState> createRecruitmentProposalSagaManager(RecruitmentProposalCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<ApprovalProposalState> approvalProposalStateSagaManager(ApprovalRecruitmentProposalSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<ApprovalDecisionState> approvalDecisionStateSagaManager(ApprovalRecruitmentDecisionSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<DecisionCreateState> createDecisionStateSagaManager(RecruitmentDecisionCreateSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

    @Bean
    public SagaManager<SaveOnboardStatusState> saveOnboardStatusStateSagaManager(SaveOnboardStatusSaga saga) {
        return new SagaManagerImpl<>(saga);
    }

}
