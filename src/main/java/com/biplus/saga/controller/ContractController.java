package com.biplus.saga.controller;

import com.biplus.saga.api.ContractApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;
import com.biplus.saga.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ContractController implements ContractApi {

    @Autowired
    private ContractService contractService;

    @Override
    public CompletableFuture<BaseResponse> updateLaborContract(Long laborContractId, UpdateLaborContractRequest request, MultipartFile contractFile) throws IOException {
        request.setContractId(laborContractId);
        request.setFileContract(contractFile == null ? null : contractFile.getBytes());
        request.setFileName(contractFile == null ? null : contractFile.getOriginalFilename());
        return contractService.updateContract(request);
    }
}
