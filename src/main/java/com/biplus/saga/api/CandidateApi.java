package com.biplus.saga.api;

import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.dto.contract.LaborContractHistoryDTO;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@Api(value = "Candidate API")
@RequestMapping("/saga")
public interface CandidateApi {
    @ApiOperation(value = "Assign candidate")
    @PostMapping(value = "/candidate/assign")
    CompletableFuture<BaseResponse> assign(@Valid @RequestBody AssignCandidateRequest request);
}
