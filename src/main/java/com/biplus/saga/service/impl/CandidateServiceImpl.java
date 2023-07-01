package com.biplus.saga.service.impl;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.saga.common.Constants;
import com.biplus.saga.domain.dto.ActionType;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;
import com.biplus.saga.service.CandidateService;
import com.biplus.saga.tramsaga.SagaCompletableTable;
import com.biplus.saga.tramsaga.state.CandidateAssignState;
import com.biplus.saga.tramsaga.state.UpdateLaborContractHistoryState;
import io.eventuate.tram.sagas.orchestration.SagaInstance;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    @Value("${candidate.root_path}")
    private String webRootPath;
    @Value("${candidate.detail_link}")
    private String candidateDetailLink;

    private final SagaManager<CandidateAssignState> candidateAssignStateSagaManager;

    private final SagaCompletableTable sagaCompletableTable;
    @Override
    public CompletableFuture<BaseResponse> assign(AssignCandidateRequest request) {
        CompletableFuture<BaseResponse> completableFuture = new CompletableFuture<>();
        try {
            CandidateAssignState sagaSate = new CandidateAssignState(request, webRootPath, candidateDetailLink);
            SagaInstance sagaInstance = candidateAssignStateSagaManager.create(sagaSate);
            sagaCompletableTable.put(ActionType.CANDIDATE_ASSIGN_CHANNEL, sagaInstance.getId(), completableFuture);
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
