package com.biplus.saga.domain.request.recruitment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaveInterviewContactRequest {

    private Long candidateId;
    private String contactTime;
    private Integer candidateResponse;
    private String note;

}
