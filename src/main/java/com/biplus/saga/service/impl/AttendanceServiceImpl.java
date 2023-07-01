package com.biplus.saga.service.impl;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.attendance.SendRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.attendance.CreateOrUpdateWorkOutsideReq;
import com.biplus.saga.domain.request.attendance.ConfirmManyRequestAttendanceReq;
import com.biplus.saga.domain.request.salary.UpdateWorkingTimeReq;
import com.biplus.saga.service.AttendanceService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.attendance.SendRequestWorkOutsideState;
import com.biplus.saga.tramsaga.state.attendance.SendRequestEditWorkingTimeState;
import com.biplus.saga.tramsaga.state.attendance.ConfirmManyRequestEditWorkingTimeState;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideCreateState;
import com.biplus.saga.tramsaga.state.attendance.WorkOutsideUpdateState;
import com.biplus.saga.tramsaga.state.attendance.ConfirmWorkOutsideState;
import com.biplus.saga.tramsaga.state.attendance.WorkingTimeCreateState;
import com.biplus.saga.tramsaga.state.attendance.WorkingTimeUpdateState;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final SagaCompletableTable sagaCompletableTable;
    private final SagaManager<SendRequestEditWorkingTimeState> sendRequestEditWorkingTimeStateSagaManager;
    private final SagaManager<WorkingTimeCreateState> workingTimeCreateStateSagaManager;
    private final SagaManager<WorkingTimeUpdateState> workingTimeUpdateStateSagaManager;
    private final SagaManager<ConfirmManyRequestEditWorkingTimeState> confirmManyRequestEditWorkingTimeStateSagaManager;
    private final SagaManager<WorkOutsideCreateState> workOutsideCreateStateSagaManager;
    private final SagaManager<WorkOutsideUpdateState> workOutsideUpdateStateSagaManager;
    private final SagaManager<SendRequestWorkOutsideState> sendRequestWorkOutsideStateSagaManager;
    private final SagaManager<ConfirmWorkOutsideState> confirmWorkOutsideStateStateSagaManager;

    @Override
    public CompletableFuture<BaseResponse> create(UpdateWorkingTimeReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            WorkingTimeCreateState sagaSate = new WorkingTimeCreateState(request);
            SagaInstance sagaInstance = workingTimeCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CREATE_WORKING_TIME_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> update(UpdateWorkingTimeReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            WorkingTimeUpdateState sagaSate = new WorkingTimeUpdateState(request);
            SagaInstance sagaInstance = workingTimeUpdateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_UPDATE_WORKING_TIME_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> sendRequestEditWorkingTime(SendRequestAttendanceReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SendRequestEditWorkingTimeState sagaSate = new SendRequestEditWorkingTimeState(request);
            SagaInstance sagaInstance = sendRequestEditWorkingTimeStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_SEND_REQUEST_EDIT_WORKING_TIME_CHANNEL,
                    sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> confirmManyRequestEditWorkingTime(ConfirmManyRequestAttendanceReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            ConfirmManyRequestEditWorkingTimeState sagaSate = new ConfirmManyRequestEditWorkingTimeState(request);
            SagaInstance sagaInstance = confirmManyRequestEditWorkingTimeStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CONFIRM_MAMY_REQUEST_EDIT_WORKING_TIME_CHANNEL,
                    sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> createWorkOutside(CreateOrUpdateWorkOutsideReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            WorkOutsideCreateState sagaSate = new WorkOutsideCreateState(request);
            SagaInstance sagaInstance = workOutsideCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CREATE_WORK_OUTSIDE_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> updateWorkOutside(CreateOrUpdateWorkOutsideReq request, Long attendanceId) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            WorkOutsideUpdateState sagaSate = new WorkOutsideUpdateState(request, attendanceId);
            SagaInstance sagaInstance = workOutsideUpdateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_UPDATE_WORK_OUTSIDE_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> sendRequestWorkOutside(SendRequestAttendanceReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            SendRequestWorkOutsideState sagaSate = new SendRequestWorkOutsideState(request);
            SagaInstance sagaInstance = sendRequestWorkOutsideStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_SEND_REQUEST_WORK_OUTSIDE_CHANNEL,
                    sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> confirmManyRequestWorkOutside(ConfirmManyRequestAttendanceReq request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            ConfirmWorkOutsideState sagaSate = new ConfirmWorkOutsideState(request);
            SagaInstance sagaInstance = confirmWorkOutsideStateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CONFIRM_MANY_REQUEST_WORK_OUTSIDE_CHANNEL,
                    sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }
}
