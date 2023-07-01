package com.biplus.saga.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseStatus {
    private static final long serialVersionUID = 4590066832132670826L;

    private boolean success;

    private String service;

    private String code;

    private String message;

    private String note;
}