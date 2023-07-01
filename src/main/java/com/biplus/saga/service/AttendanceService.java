package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.CreateOrUpdateWorkOutsideReq;
import com.biplus.saga.domain.request.attendance.SendRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.salary.UpdateWorkingTimeReq;

import java.util.concurrent.CompletableFuture;

public interface AttendanceService {
    CompletableFuture<BaseResponse> create(UpdateWorkingTimeReq request);

    CompletableFuture<BaseResponse> update(UpdateWorkingTimeReq request);

    CompletableFuture<BaseResponse> sendRequestEditWorkingTime(SendRequestAttendanceReq request);

    CompletableFuture<BaseResponse> confirmManyRequestEditWorkingTime(ConfirmManyRequestAttendanceReq request);

    CompletableFuture<BaseResponse> createWorkOutside(CreateOrUpdateWorkOutsideReq request);

    CompletableFuture<BaseResponse> updateWorkOutside(CreateOrUpdateWorkOutsideReq request, Long attendanceId);

    CompletableFuture<BaseResponse> sendRequestWorkOutside(SendRequestAttendanceReq request);

    CompletableFuture<BaseResponse> confirmManyRequestWorkOutside(ConfirmManyRequestAttendanceReq request);
}
