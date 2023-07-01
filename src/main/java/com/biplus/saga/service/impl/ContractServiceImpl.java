package com.biplus.saga.service.impl;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;
import com.biplus.saga.service.ContractService;
import com.biplus.saga.service.RedisService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.UpdateContractState;
import com.biplus.saga.tramsaga.state.UpdateLaborContractHistoryState;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final SagaManager<UpdateLaborContractHistoryState> updateLaborContractHistoryStateManager;

    private final SagaManager<UpdateContractState> updateContractStateSagaManager;

    private final SagaCompletableTable sagaCompletableTable;

    private final RedisService redisService;

    @Override
    public CompletableFuture<BaseResponse> updateLaborContractHistory(Long laborContractId, LaborContractHistoryDTO laborContractHistoryDTO) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            List<String> listRoleCurrentUser = redisService.getRoles();
            UpdateLaborContractHistoryState sagaSate = new UpdateLaborContractHistoryState(laborContractId, laborContractHistoryDTO, listRoleCurrentUser);
            SagaInstance sagaInstance = updateLaborContractHistoryStateManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.UPDATE_LABOR_CONTRACT_HISTORY_CHANNEL, sagaInstance.getId(), completableFuture);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new IllegalArgumentException(e.getMessage(), e));
        } catch (RuntimeException |IOException e) {
            log.error(e.getMessage(), e);
            completableFuture.completeExceptionally(new InternalServerErrorException(Constants.Message.ERROR_SYSTEM, e));
        }
        return completableFuture;
    }

    @Override
    public CompletableFuture<BaseResponse> updateContract(UpdateLaborContractRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            UpdateContractState sagaSate = new UpdateContractState(request);
            SagaInstance sagaInstance = updateContractStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.CONTRACT_UPDATE_CHANNEL, sagaInstance.getId(), completableFuture);
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
