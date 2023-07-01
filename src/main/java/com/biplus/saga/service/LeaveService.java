package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.leave.*;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface LeaveService {
    CompletableFuture<BaseResponse> create(LeaveRegistrationRequest request);

    CompletableFuture<BaseResponse> update(LeaveUpdateRequest request);

    CompletableFuture<BaseResponse> approve(SendRequestLeavesRequest request);

    CompletableFuture<BaseResponse> cancel(CancelLeaveRequest request);

    CompletableFuture<BaseResponse> confirm(ConfirmListOfLeavesRequest request);
}
