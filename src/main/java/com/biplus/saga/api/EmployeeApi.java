package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Api(value = "Employee API")
@RequestMapping("/saga")
public interface EmployeeApi {
    @ApiOperation(value = "create employee")
    @PostMapping(value = "/employee/create")
    CompletableFuture<BaseResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request);

    @ApiOperation(value = "Approval employee")
    @PostMapping(value = "/employee/update-employee-history/{id}")
    CompletableFuture<BaseResponse> approvalEmployee(@PathVariable(value = "id", required = false) final Long employeeId,
                                                   @RequestBody @Valid EmployeeHistoryRequest request);
}
