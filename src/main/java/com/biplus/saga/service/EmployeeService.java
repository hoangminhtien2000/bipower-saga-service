package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;

import java.util.concurrent.CompletableFuture;

public interface EmployeeService {
    CompletableFuture<BaseResponse> createEmployee(CreateEmployeeRequest request);

    CompletableFuture<BaseResponse> approvalEmployee(Long employeeId, EmployeeHistoryRequest request);
}
