package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.recruitment.*;

import java.util.concurrent.CompletableFuture;

public interface RecruitmentService {

    CompletableFuture<BaseResponse> saveInterviewContact(SaveInterviewContactRequest request);

    CompletableFuture<BaseResponse> saveInterviewSchedule(SaveInterviewScheduleRequest request);

    CompletableFuture<BaseResponse> assignCVReviewer(AssignCVReviewerRequest request);

    CompletableFuture<BaseResponse> removeCVReviewer(DeleteCVReviewerRequest request);

    CompletableFuture<BaseResponse> interviewEvaluation(SaveInterviewResultRequest request);

    CompletableFuture<BaseResponse> saveCVReview(SavingCvReviewRequest request);

    CompletableFuture<BaseResponse> createRecruitmentProposal(CreatingRecruitmentProposalRequest request);

    CompletableFuture<BaseResponse> approvalRecruitmentProposal(ApprovalRecruitmentProposalRequest request);

    CompletableFuture<BaseResponse> createRecruitmentDecision(CreatingRecruitmentDecisionRequest request);

    CompletableFuture<BaseResponse> approvalRecruitmentDecision(ApprovalRecruitmentDecisionRequest request);

    CompletableFuture<BaseResponse> saveOnboardStatus(SaveOnboardStatusRequest request);
}
