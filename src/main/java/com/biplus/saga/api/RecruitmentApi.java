package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.recruitment.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Api(value = "Recruitment API")
@RequestMapping("/saga")
public interface RecruitmentApi {

    @ApiOperation(value = "Assign cv reviewer")
    @PostMapping(value = "/recruitment/assign-cv-reviewer")
    CompletableFuture<BaseResponse> assignCVReviewer(@Valid @RequestBody AssignCVReviewerRequest request);

    @ApiOperation(value = "Remove cv reviewer")
    @PostMapping(value = "/recruitment/remove-cv-reviewer")
    CompletableFuture<BaseResponse> removeCVReviewer(@Valid @RequestBody DeleteCVReviewerRequest request);

    @ApiOperation(value = "save cv review")
    @PostMapping(value = "/recruitment/review-cv")
    CompletableFuture<BaseResponse> saveCVReview(@Valid @RequestBody SavingCvReviewRequest request);

    @ApiOperation(value = "Save interview contact")
    @PostMapping("/recruitment/save-interview-contact")
    CompletableFuture<BaseResponse> saveInterviewContact(@RequestBody SaveInterviewContactRequest request);

    @ApiOperation(value = "Save interview schedule")
    @PostMapping("/recruitment/save-interview-schedule")
    CompletableFuture<BaseResponse> saveInterviewSchedule(@RequestBody SaveInterviewScheduleRequest request);

    @ApiOperation(value = "Interview evaluation")
    @PostMapping("/recruitment/interview-evaluation")
    CompletableFuture<BaseResponse> interviewEvaluation(@RequestBody SaveInterviewResultRequest request);

    @ApiOperation(value = "Create recruitment proposal")
    @PostMapping("/recruitment/create-recruitment-proposal")
    CompletableFuture<BaseResponse> createRecruitmentProposal(@RequestBody CreatingRecruitmentProposalRequest request);

    @ApiOperation(value = "Approval proposal")
    @PostMapping("/recruitment/approval-proposal")
    CompletableFuture<BaseResponse> approvalProposal(@RequestBody ApprovalRecruitmentProposalRequest request);

    @ApiOperation(value = "Create recruitment proposal")
    @PostMapping("/recruitment/create-decision")
    CompletableFuture<BaseResponse> createRecruitmentDecision(@RequestBody CreatingRecruitmentDecisionRequest request);

    @ApiOperation(value = "Approval decision")
    @PostMapping("/recruitment/approval-decision")
    CompletableFuture<BaseResponse> approvalDecision(@RequestBody ApprovalRecruitmentDecisionRequest request);

    @ApiOperation(value = "Save onboard status")
    @PostMapping("/recruitment/save-onboard-status")
    CompletableFuture<BaseResponse> saveOnboardStatus(@RequestBody SaveOnboardStatusRequest request);
}
