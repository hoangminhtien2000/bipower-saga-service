package com.biplus.saga.controller;

import com.biplus.saga.api.RecruitmentApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.recruitment.*;
import com.biplus.saga.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecruitmentController implements RecruitmentApi {

    private final RecruitmentService recruitmentService;

    @Override
    public CompletableFuture<BaseResponse> saveInterviewContact(SaveInterviewContactRequest request) {
        return recruitmentService.saveInterviewContact(request);
    }

    @Override
    public CompletableFuture<BaseResponse> assignCVReviewer(AssignCVReviewerRequest request) {
        return recruitmentService.assignCVReviewer(request);
    }

    @Override
    public CompletableFuture<BaseResponse> removeCVReviewer(DeleteCVReviewerRequest request) {
        return recruitmentService.removeCVReviewer(request);
    }
    @Override
    public CompletableFuture<BaseResponse> saveCVReview(SavingCvReviewRequest request) {
        return recruitmentService.saveCVReview(request);
    }

    @Override
    public CompletableFuture<BaseResponse> saveInterviewSchedule(SaveInterviewScheduleRequest request) {
        return recruitmentService.saveInterviewSchedule(request);
    }

    @Override
    public CompletableFuture<BaseResponse> interviewEvaluation(SaveInterviewResultRequest request) {
        return recruitmentService.interviewEvaluation(request);
    }

    @Override
    public CompletableFuture<BaseResponse> createRecruitmentProposal(CreatingRecruitmentProposalRequest request) {
        return recruitmentService.createRecruitmentProposal(request);
    }

    @Override
    public CompletableFuture<BaseResponse> approvalProposal(ApprovalRecruitmentProposalRequest request) {
        return recruitmentService.approvalRecruitmentProposal(request);
    }

    @Override
    public CompletableFuture<BaseResponse> createRecruitmentDecision(CreatingRecruitmentDecisionRequest request) {
        return recruitmentService.createRecruitmentDecision(request);
    }

    @Override
    public CompletableFuture<BaseResponse> approvalDecision(ApprovalRecruitmentDecisionRequest request) {
        return recruitmentService.approvalRecruitmentDecision(request);
    }


    @Override
    public CompletableFuture<BaseResponse> saveOnboardStatus(SaveOnboardStatusRequest request) {
        return recruitmentService.saveOnboardStatus(request);
    }
}
