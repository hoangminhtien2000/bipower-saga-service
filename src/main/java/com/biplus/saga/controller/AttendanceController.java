package com.biplus.saga.controller;

import com.biplus.saga.api.AttendanceApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.attendance.SendRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.CreateOrUpdateWorkOutsideReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.salary.UpdateWorkingTimeReq;
import com.biplus.saga.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendanceController implements AttendanceApi {
    private final AttendanceService attendanceService;

    @Override
    public CompletableFuture<BaseResponse> createRequestAddNewWorkingTime(UpdateWorkingTimeReq request) {
        return attendanceService.create(request);
    }

    @Override
    public CompletableFuture<BaseResponse> updateWorkingTimeReq(UpdateWorkingTimeReq request) {
        return attendanceService.update(request);
    }

    @Override
    public CompletableFuture<BaseResponse> sendRequestEditWorkingTime(SendRequestAttendanceReq request) {
        return attendanceService.sendRequestEditWorkingTime(request);
    }

    @Override
    public CompletableFuture<BaseResponse> confirmManyRequestEditWorkingTime(ConfirmManyRequestAttendanceReq request) {
        return attendanceService.confirmManyRequestEditWorkingTime(request);
    }

    @Override
    public CompletableFuture<BaseResponse> createWorkOutside(CreateOrUpdateWorkOutsideReq request) {
        return attendanceService.createWorkOutside(request);
    }

    @Override
    public CompletableFuture<BaseResponse> updateWorkOutside(CreateOrUpdateWorkOutsideReq request, Long attendanceId) {
        return attendanceService.updateWorkOutside(request, attendanceId);
    }

    @Override
    public CompletableFuture<BaseResponse> sendRequestWorkOutside(SendRequestAttendanceReq request) {
        return attendanceService.sendRequestWorkOutside(request);
    }

    @Override
    public CompletableFuture<BaseResponse> confirmManyRequestWorkOutside(ConfirmManyRequestAttendanceReq request) {
        return attendanceService.confirmManyRequestWorkOutside(request);
    }
}
