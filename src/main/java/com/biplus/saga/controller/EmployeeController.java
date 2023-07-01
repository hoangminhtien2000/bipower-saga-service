package com.biplus.saga.controller;

import com.biplus.saga.api.EmployeeApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;
import com.biplus.saga.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
    @Autowired
    private EmployeeService employeeService;
    @Override
    public CompletableFuture<BaseResponse> createEmployee(@Valid CreateEmployeeRequest request) {
        return employeeService.createEmployee(request);
    }

    @Override
    public CompletableFuture<BaseResponse> approvalEmployee(Long employeeId, EmployeeHistoryRequest request) {
        return employeeService.approvalEmployee(employeeId, request);
    }
}
