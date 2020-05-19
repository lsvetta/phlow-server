package com.phlow.server.web.errorhandling;

import com.phlow.server.domain.common.ActionForbiddenException;
import com.phlow.server.domain.common.EntityNotFoundException;
import com.phlow.server.domain.common.InvalidArgumentException;
import com.phlow.server.domain.common.UploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ApiControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException ex, WebRequest webRequest) {
        log.error("Неверный аргумент", ex);
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), webRequest);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(ActionForbiddenException.class)
    public ResponseEntity<Object> handleActionForbiddenException(ActionForbiddenException ex, WebRequest webRequest) {
        log.error("Недостаточно прав", ex);
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), webRequest);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest webRequest) {
        log.error("Запись не найдена", ex);
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), webRequest);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(UploadFailureException.class)
    public ResponseEntity<Object> handleIOExceptionException(UploadFailureException ex, WebRequest webRequest) {
        log.error("Возникла ошибка при орбаботке файла", ex);
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), webRequest);
    }
}
