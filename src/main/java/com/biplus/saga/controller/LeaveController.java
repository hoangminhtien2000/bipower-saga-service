package com.biplus.saga.controller;

import antlr.ASTFactory;
import com.biplus.saga.api.LeaveApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.leave.*;
import com.biplus.saga.service.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LeaveController implements LeaveApi {
    private final LeaveService leaveService;

    @Override
    public CompletableFuture<BaseResponse> createLeave(LeaveRegistrationRequest request) {
        return leaveService.create(request);
    }

    @Override
    public CompletableFuture<BaseResponse> updateLeave(Long employeeLeaveId, LeaveUpdateRequest request) {
        request.setEmployeeLeaveId(employeeLeaveId);
        return leaveService.update(request);
    }

    @Override
    public CompletableFuture<BaseResponse> approveLeave(SendRequestLeavesRequest request) {
        return leaveService.approve(request);
    }

    @Override
    public CompletableFuture<BaseResponse> cancelLeave(CancelLeaveRequest request) {
        return leaveService.cancel(request);
    }

    @Override
    public CompletableFuture<BaseResponse> confirmListOfLeaveRequest(ConfirmListOfLeavesRequest request) {
        return leaveService.confirm(request);
    }
}
