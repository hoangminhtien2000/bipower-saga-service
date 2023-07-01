package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@Api(value = "Saga API")
@RequestMapping
public interface SagaApi {
    @ApiOperation(value = "Recover good from channel")
    @PostMapping(value = "/saga/contract/approval/{id}")
    CompletableFuture<BaseResponse> updateLaborContractHistory(@PathVariable(value = "id", required = false) final Long laborContractId,
                                                               @NotNull @RequestBody LaborContractHistoryDTO laborContractHistoryDTO);
}
