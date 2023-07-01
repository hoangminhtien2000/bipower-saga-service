package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.contract.UpdateLaborContractRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Api(value = "Contract API")
@RequestMapping("/saga")
public interface ContractApi {
    @ApiOperation(value = "update contract")
    @PostMapping(value = "/contract/update/{id}")
    CompletableFuture<BaseResponse> updateLaborContract(@PathVariable(value = "id", required = false) final Long laborContractId,
                                                   @Valid @RequestPart UpdateLaborContractRequest request,
                                                   @RequestParam(value = "contractFile", required = false) MultipartFile contractFile) throws IOException;
}
