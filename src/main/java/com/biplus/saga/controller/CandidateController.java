package com.biplus.saga.controller;

import com.biplus.saga.api.CandidateApi;
import com.biplus.saga.domain.dto.BaseResponse;
import com.biplus.saga.domain.request.candidate.AssignCandidateRequest;
import com.biplus.saga.service.CandidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CandidateController implements CandidateApi {
    @Autowired
    private CandidateService candidateService;
    @Override
    public CompletableFuture<BaseResponse> assign(@Valid AssignCandidateRequest request) {
        return candidateService.assign(request);
    }
}
