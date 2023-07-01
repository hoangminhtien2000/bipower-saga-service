package com.biplus.saga.service.impl;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.leave.*;
import com.biplus.saga.service.LeaveService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.leave.*;
import com.biplus.saga.tramsaga.state.salary.OvertimeApproveState;
import com.biplus.saga.tramsaga.state.salary.OvertimeCreateState;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final SagaCompletableTable sagaCompletableTable;
    private final SagaManager<LeaveCreateState> leaveCreateStateSagaManager;
    private final SagaManager<LeaveUpdateState> leaveUpdateStateSagaManager;
    private final SagaManager<LeaveApproveState> leaveApproveStateSagaManager;
    private final SagaManager<LeaveCancelState> leaveCancelStateSagaManager;
    private final SagaManager<LeaveConfirmState> leaveConfirmStateSagaManager;

    @Override
    public CompletableFuture<BaseResponse> create(LeaveRegistrationRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            LeaveCreateState sagaSate = new LeaveCreateState(request);
            SagaInstance sagaInstance = leaveCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CREATE_LEAVE_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> update(LeaveUpdateRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            LeaveUpdateState sagaSate = new LeaveUpdateState(request);
            SagaInstance sagaInstance = leaveUpdateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_UPDATE_LEAVE_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> approve(SendRequestLeavesRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            LeaveApproveState sagaSate = new LeaveApproveState(request);
            SagaInstance sagaInstance = leaveApproveStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_APPROVE_LEAVE_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> cancel(CancelLeaveRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            LeaveCancelState sagaSate = new LeaveCancelState(request);
            SagaInstance sagaInstance = leaveCancelStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CANCEL_LEAVE_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> confirm(ConfirmListOfLeavesRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            LeaveConfirmState sagaSate = new LeaveConfirmState(request);
            SagaInstance sagaInstance = leaveConfirmStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CONFIRM_LEAVE_CHANNEL, sagaInstance.getId(), completableFuture);
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
