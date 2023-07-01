package com.biplus.saga.service;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;

import java.util.concurrent.CompletableFuture;

public interface ContractService {
    CompletableFuture<BaseResponse> updateLaborContractHistory(Long laborContractId, LaborContractHistoryDTO laborContractHistoryDTO);

    CompletableFuture<BaseResponse> updateContract(UpdateLaborContractRequest request);
}
