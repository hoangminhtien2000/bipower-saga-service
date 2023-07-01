package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.SendRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.CreateOrUpdateWorkOutsideReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.salary.UpdateWorkingTimeReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Api(value = "Salary attendance API")
@RequestMapping("/saga/salary/attendance")
public interface AttendanceApi {
    @ApiOperation(value = "Api create woking time")
    @PostMapping(value = "/create-request-add-new-working-time")
    CompletableFuture<BaseResponse> createRequestAddNewWorkingTime(@RequestBody @Valid UpdateWorkingTimeReq req);

    @ApiOperation(value = "Api update working time")
    @PostMapping(value = "/create-or-update-request-edit-working-time")
    CompletableFuture<BaseResponse> updateWorkingTimeReq(@RequestBody @Valid UpdateWorkingTimeReq req);

    @ApiOperation(value = "Api send request edit working time")
    @PostMapping(value = "/send-request-edit-working-time")
    CompletableFuture<BaseResponse> sendRequestEditWorkingTime(@RequestBody @Valid SendRequestAttendanceReq req);

    @ApiOperation(value = "Api comfirm many request edit working time")
    @PostMapping(value = "/confirm-many-request-edit-working-time")
    CompletableFuture<BaseResponse> confirmManyRequestEditWorkingTime(@RequestBody @Valid ConfirmManyRequestAttendanceReq req);


    @ApiOperation(value = "Api create work outside")
    @PostMapping(value = "/create-work-outside")
    CompletableFuture<BaseResponse> createWorkOutside(@RequestBody @Valid CreateOrUpdateWorkOutsideReq req);

    @ApiOperation(value = "Api update work outside")
    @PutMapping(value = "/update-work-outside/{attendanceId}")
    CompletableFuture<BaseResponse> updateWorkOutside(@RequestBody @Valid CreateOrUpdateWorkOutsideReq req, @PathVariable Long attendanceId);

    @ApiOperation(value = "Api send request work outside")
    @PostMapping(value = "/send-request-work-outside")
    CompletableFuture<BaseResponse> sendRequestWorkOutside(@RequestBody @Valid SendRequestAttendanceReq req);

    @ApiOperation(value = "Api comfirm many request work outside")
    @PostMapping(value = "/confirm-many-request-work-outside")
    CompletableFuture<BaseResponse> confirmManyRequestWorkOutside(@RequestBody @Valid ConfirmManyRequestAttendanceReq req);
}
