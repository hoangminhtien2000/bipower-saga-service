package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;

import java.util.concurrent.CompletableFuture;

public interface CandidateService {
    CompletableFuture<BaseResponse> assign(AssignCandidateRequest request);
}
