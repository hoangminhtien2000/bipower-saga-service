package com.biplus.saga.service.impl;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.employee.CreateEmployeeRequest;
import com.biplus.saga.domain.request.employee.EmployeeHistoryRequest;
import com.biplus.saga.service.EmployeeService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.employee.EmployeeApprovalState;
import com.biplus.saga.tramsaga.state.employee.EmployeeCreateState;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final SagaManager<EmployeeCreateState> employeeCreateStateSagaManager;
    private final SagaManager<EmployeeApprovalState> employeeApprovalStateSagaManager;

    private final SagaCompletableTable sagaCompletableTable;
    @Override
    public CompletableFuture<BaseResponse> createEmployee(CreateEmployeeRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            EmployeeCreateState sagaSate = new EmployeeCreateState(request);
            SagaInstance sagaInstance = employeeCreateStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.EMPLOYEE_CREATE_CHANNEL, sagaInstance.getId(), completableFuture);
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
    public CompletableFuture<BaseResponse> approvalEmployee(Long employeeId, EmployeeHistoryRequest request) {
        request.setEmployeeId(employeeId);
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            EmployeeApprovalState sagaSate = new EmployeeApprovalState(request);
            SagaInstance sagaInstance = employeeApprovalStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.EMPLOYEE_APPROVAL_CHANNEL, sagaInstance.getId(), completableFuture);
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
