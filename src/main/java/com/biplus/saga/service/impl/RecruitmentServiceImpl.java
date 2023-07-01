package com.biplus.saga.service.impl;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.recruitment.*;
import com.biplus.saga.service.RecruitmentService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.InterviewEvaluationState;
import com.biplus.saga.tramsaga.state.SaveInterviewContactState;
import com.biplus.saga.tramsaga.state.SaveInterviewScheduleState;
import com.biplus.saga.tramsaga.state.recruitment.*;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentServiceImpl implements RecruitmentService {
    @Value("${candidate.root_path}")
    private String webRootPath;
    @Value("${candidate.detail_link}")
    private String candidateDetailLink;

    private final SagaManager<SaveInterviewContactState> saveInterviewContactStateSagaManager;
    private final SagaManager<SaveInterviewScheduleState> saveInterviewScheduleStateSagaManager;
    private final SagaManager<InterviewEvaluationState> interviewEvaluationSagaManager;
    private final SagaManager<CVAssignReviewerState> assignCVReviewerStateSagaManager;
    private final SagaManager<DeleteCVReviewerState> deleteCVReviewerStateSagaManager;
    private final SagaManager<SavingCVReviewState> savingCVReviewStateSagaManager;
    private final SagaManager<RecruitmentProposalCreateState> recruitmentProposalCreateStateSagaManager;
    private final SagaManager<ApprovalProposalState> approvalProposalStateSagaManager;
    private final SagaManager<DecisionCreateState> createDecisionStateSagaManager;
    private final SagaManager<ApprovalDecisionState> approvalDecisionStateSagaManager;
    private final SagaManager<SaveOnboardStatusState> saveOnboardStatusStateSagaManager;

    private final SagaCompletableTable sagaCompletableTable;

    @Override
    public CompletableFuture<BaseResponse> saveInterviewContact(SaveInterviewContactRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SaveInterviewContactState sagaSate = new SaveInterviewContactState(request);
            SagaInstance sagaInstance = saveInterviewContactStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SAVE_INTERVIEW_CONTACT_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> saveInterviewSchedule(SaveInterviewScheduleRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SaveInterviewScheduleState sagaSate = new SaveInterviewScheduleState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = saveInterviewScheduleStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SAVE_INTERVIEW_SCHEDULE_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> assignCVReviewer(AssignCVReviewerRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            CVAssignReviewerState sagaSate = new CVAssignReviewerState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = assignCVReviewerStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.ASSIGN_CV_REVIEWER_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> removeCVReviewer(DeleteCVReviewerRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            DeleteCVReviewerState sagaSate = new DeleteCVReviewerState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = deleteCVReviewerStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.DELETE_CV_REVIEWER_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> interviewEvaluation(SaveInterviewResultRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            InterviewEvaluationState sagaSate = new InterviewEvaluationState(request);
            SagaInstance sagaInstance = interviewEvaluationSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.INTERVIEW_EVALUATION_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> saveCVReview(SavingCvReviewRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SavingCVReviewState sagaSate = new SavingCVReviewState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = savingCVReviewStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SAVE_CV_REVIEW_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> createRecruitmentProposal(CreatingRecruitmentProposalRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            RecruitmentProposalCreateState sagaSate = new RecruitmentProposalCreateState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = recruitmentProposalCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.RECRUITMENT_PROPOSAL_CREATE_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> approvalRecruitmentProposal(ApprovalRecruitmentProposalRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            ApprovalProposalState sagaSate = new ApprovalProposalState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = approvalProposalStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.APPROVAL_RECRUITMENT_PROPOSAL_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> createRecruitmentDecision(CreatingRecruitmentDecisionRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            DecisionCreateState sagaSate = new DecisionCreateState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = createDecisionStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.RECRUITMENT_DECISION_CREATE_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> approvalRecruitmentDecision(ApprovalRecruitmentDecisionRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            ApprovalDecisionState sagaSate = new ApprovalDecisionState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = approvalDecisionStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.APPROVAL_RECRUITMENT_DECISION_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> saveOnboardStatus(SaveOnboardStatusRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SaveOnboardStatusState sagaSate = new SaveOnboardStatusState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = saveOnboardStatusStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SAVE_ONBOARD_STATUS, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }
}
