package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.salary.ConfirmManyOvertimeRequest;
import com.biplus.saga.domain.request.salary.CreateOvertimeRequest;
import com.biplus.saga.domain.request.salary.OvertimeToApproverRequest;
import com.biplus.saga.domain.request.salary.UpdateOvertimeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Api(value = "Salary API")
@RequestMapping("/saga/salary/overtime")
public interface SalaryApi {
    @ApiOperation(value = "Api create overtime")
    @PostMapping(value = "/create")
    CompletableFuture<BaseResponse> createOvertime(@RequestBody @Valid CreateOvertimeRequest request);

    @ApiOperation(value = "Api update overtime")
    @PostMapping(value = "/update/{overtimeId}")
    CompletableFuture<BaseResponse> updateOvertime(@PathVariable("overtimeId") Long overtimeId,
                                                   @RequestBody @Valid UpdateOvertimeRequest request);

    @ApiOperation(value = "Api send many overtime to approve")
    @PostMapping(value = "/send-many-overtime-req-to-approver")
    CompletableFuture<BaseResponse> approveOvertimes(@RequestBody @Valid OvertimeToApproverRequest request);

    @ApiOperation(value = "Api send many overtime to approve")
    @PostMapping(value = "/confirm-many-overtime-request")
    CompletableFuture<BaseResponse> confirmOvertimes(@RequestBody @Valid ConfirmManyOvertimeRequest request);
}
