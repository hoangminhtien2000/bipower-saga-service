package com.biplus.saga.domain.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponse<T> {
    private static final long serialVersionUID = 4590066832132670826L;

    private ResponseStatus status;

    private T data;
}