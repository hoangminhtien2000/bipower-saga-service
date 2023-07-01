package com.biplus.saga.controller;

import com.biplus.saga.api.SalaryApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.salary.ConfirmManyOvertimeRequest;
import com.biplus.saga.domain.request.salary.CreateOvertimeRequest;
import com.biplus.saga.domain.request.salary.OvertimeToApproverRequest;
import com.biplus.saga.domain.request.salary.UpdateOvertimeRequest;
import com.biplus.saga.service.SalaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SalaryController implements SalaryApi {
    @Autowired
    private SalaryService salaryService;

    @Override
    public CompletableFuture<BaseResponse> createOvertime(CreateOvertimeRequest request) {
        return salaryService.createOvertime(request);
    }

    @Override
    public CompletableFuture<BaseResponse> updateOvertime(Long overtimeId, UpdateOvertimeRequest request) {
        request.setOvertimeId(overtimeId);
        return salaryService.updateOvertime(request);
    }

    @Override
    public CompletableFuture<BaseResponse> approveOvertimes(OvertimeToApproverRequest request) {
        return salaryService.approveOvertimes(request);
    }

    @Override
    public CompletableFuture<BaseResponse> confirmOvertimes(ConfirmManyOvertimeRequest request) {
        return salaryService.confirmOvertimes(request);
    }
}
