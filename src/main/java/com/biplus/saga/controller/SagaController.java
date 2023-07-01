package com.biplus.saga.controller;

import com.biplus.saga.api.SagaApi;
import com.biplus.saga.domain.dto.*;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import com.biplus.saga.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SagaController implements SagaApi {
    @Value("${spring.mvc.async.request-timeout:1800000}")
    private int timeout;

    @Autowired
    private ContractService contractService;

    @Override
    public CompletableFuture<BaseResponse> updateLaborContractHistory(Long laborContractId, LaborContractHistoryDTO laborContractHistoryDTO) {
        //TODO
        return contractService.updateLaborContractHistory(laborContractId, laborContractHistoryDTO)
                .orTimeout(timeout, MILLISECONDS);
    }
}
