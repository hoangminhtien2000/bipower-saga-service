package com.biplus.saga.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse implements Serializable {
    private boolean success;
    private String message;
    private String requestId;

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
