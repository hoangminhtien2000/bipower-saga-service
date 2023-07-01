package com.biplus.saga.controller;

import com.biplus.core.exception.InternalServerErrorException;
import com.biplus.core.message.ErrorResponse;
import com.biplus.core.message.FieldInvalidError;
import com.biplus.core.message.MessageBundle;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {


    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseBody
    protected ErrorResponse handleInternalServerErrorException(InternalServerErrorException ex) {
        log.error("handleInternalServerErrorException", ex);
        return new ErrorResponse(INTERNAL_SERVER_ERROR, MessageBundle.getMessage(ex.getMessage()), ex);
    }

    @ResponseStatus(REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    @ResponseBody
    protected ErrorResponse handleSagaTimeoutException(TimeoutException ex) {
        log.error("handleInternalServerErrorException", ex);
        return new ErrorResponse(HttpStatus.REQUEST_TIMEOUT, MessageBundle.getMessage("error.timeout"), ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("handleInternalServerErrorException", ex);
        var errorResponse = new ErrorResponse(BAD_REQUEST, MessageBundle.getMessage("error.argument.invalid"), ex);
        BindingResult result = ex.getBindingResult();
        result.getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .map(error -> new FieldInvalidError(error.getObjectName(), error.getField(), error.getDefaultMessage()))
                .forEach(errorResponse::addDetailError);
        result.getAllErrors().forEach(i -> System.out.println(JSonMapper.toJson(i)));
        return errorResponse;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    protected ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        log.error("handleInternalServerErrorException", ex);
        return new ErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

}
