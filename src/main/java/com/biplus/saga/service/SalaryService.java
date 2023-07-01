package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.salary.ConfirmManyOvertimeRequest;
import com.biplus.saga.domain.request.salary.CreateOvertimeRequest;
import com.biplus.saga.domain.request.salary.OvertimeToApproverRequest;
import com.biplus.saga.domain.request.salary.UpdateOvertimeRequest;

import java.util.concurrent.CompletableFuture;

public interface SalaryService {
    CompletableFuture<BaseResponse> createOvertime(CreateOvertimeRequest request);

    CompletableFuture<BaseResponse> updateOvertime(UpdateOvertimeRequest request);

    CompletableFuture<BaseResponse> approveOvertimes(OvertimeToApproverRequest request);

    CompletableFuture<BaseResponse> confirmOvertimes(ConfirmManyOvertimeRequest request);
}
