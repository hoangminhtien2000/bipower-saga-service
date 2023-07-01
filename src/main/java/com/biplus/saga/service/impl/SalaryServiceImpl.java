package com.biplus.saga.service.impl;

import antlr.ASTFactory;
import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.salary.ConfirmManyOvertimeRequest;
import com.biplus.saga.domain.request.salary.CreateOvertimeRequest;
import com.biplus.saga.domain.request.salary.OvertimeToApproverRequest;
import com.biplus.saga.domain.request.salary.UpdateOvertimeRequest;
import com.biplus.saga.service.SalaryService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.employee.EmployeeApprovalState;
import com.biplus.saga.tramsaga.state.employee.EmployeeCreateState;
import com.biplus.saga.tramsaga.state.salary.OvertimeApproveState;
import com.biplus.saga.tramsaga.state.salary.OvertimeConfirmState;
import com.biplus.saga.tramsaga.state.salary.OvertimeCreateState;
import com.biplus.saga.tramsaga.state.salary.OvertimeUpdateState;
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
public class SalaryServiceImpl implements SalaryService {

    private final SagaManager<OvertimeCreateState> overtimeCreateStateSagaManager;
    private final SagaManager<OvertimeUpdateState> overtimeUpdateStateSagaManager;
    private final SagaCompletableTable sagaCompletableTable;
    private final SagaManager<OvertimeApproveState> overtimeApproveStateSagaManager;
    private final SagaManager<OvertimeConfirmState> overtimeConfirmStateSagaManager;

    @Override
    public CompletableFuture<BaseResponse> createOvertime(CreateOvertimeRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            OvertimeCreateState sagaSate = new OvertimeCreateState(request);
            SagaInstance sagaInstance = overtimeCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CREATE_OVERTIME_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> updateOvertime(UpdateOvertimeRequest request) {

        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            OvertimeUpdateState sagaSate = new OvertimeUpdateState(request);
            SagaInstance sagaInstance = overtimeUpdateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_UPDATE_OVERTIME_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> approveOvertimes(OvertimeToApproverRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            OvertimeApproveState sagaSate = new OvertimeApproveState(request);
            SagaInstance sagaInstance = overtimeApproveStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_APPROVE_OVERTIME_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> confirmOvertimes(ConfirmManyOvertimeRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            OvertimeConfirmState sagaSate = new OvertimeConfirmState(request);
            SagaInstance sagaInstance = overtimeConfirmStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.SALARY_CONFIRM_OVERTIME_CHANNEL, sagaInstance.getId(), completableFuture);
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
