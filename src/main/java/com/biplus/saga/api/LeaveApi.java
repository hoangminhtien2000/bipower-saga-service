package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.leave.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Api(value = "Salary leave API")
@RequestMapping("/saga/salary/leave")
public interface LeaveApi {
    @ApiOperation(value = "Api create leave")
    @PostMapping(value = "/create")
    CompletableFuture<BaseResponse> createLeave(@RequestBody @Valid LeaveRegistrationRequest request);

    @ApiOperation(value = "Api update leave")
    @PostMapping(value = "/update/{employeeLeaveId}")
    CompletableFuture<BaseResponse> updateLeave(@PathVariable Long employeeLeaveId, @RequestBody @Valid LeaveUpdateRequest request);

    @ApiOperation(value = "Api approve leave")
    @PostMapping(value = "/send-request-leaves")
    CompletableFuture<BaseResponse> approveLeave(@RequestBody @Valid SendRequestLeavesRequest request);

    @ApiOperation(value = "Api cancel leave")
    @PostMapping("/send-request-cancel-leaves")
    public CompletableFuture<BaseResponse> cancelLeave(@RequestBody @Valid CancelLeaveRequest req);


    @ApiOperation(value = "Api confirm leave")
    @PostMapping("/confirm-leave-requests")
    CompletableFuture<BaseResponse> confirmListOfLeaveRequest(@RequestBody @Valid ConfirmListOfLeavesRequest req);
}
