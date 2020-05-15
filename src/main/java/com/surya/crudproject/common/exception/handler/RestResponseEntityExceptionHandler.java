package com.surya.crudproject.common.exception.handler;

import com.surya.crudproject.common.exception.BaseException;
import com.surya.crudproject.common.exception.error.AppErrorObject;
import com.surya.crudproject.common.exception.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG =
      LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    AppErrorObject errorObject =
        new AppErrorObject.Builder().appCode(ErrorCode.INVALID_DATA).HttpStatus(HttpStatus.OK)
            .build();
    LOG.error("Exception:", ex);
    return handleExceptionInternal(ex, errorObject, new HttpHeaders(), errorObject.getHttpStatus(),
        request);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
    AppErrorObject errorObject =
        new AppErrorObject.Builder().appCode(ErrorCode.ACCESS_DENIED).HttpStatus(HttpStatus.OK)
            .build();
    LOG.error("Exception:", ex);
    return handleExceptionInternal(ex, errorObject, new HttpHeaders(), errorObject.getHttpStatus(),
        request);
  }

  @ExceptionHandler(value = {BaseException.class})
  protected ResponseEntity<Object> handleBaseException(BaseException bex, WebRequest request) {
    AppErrorObject customError = bex.getCustomError();
    return handleExceptionInternal(bex, customError, new HttpHeaders(),
        bex.getCustomError().getHttpStatus(), request);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleMainException(Exception ex, WebRequest request) {
    AppErrorObject errorObject =
        new AppErrorObject.Builder().appCode(ErrorCode.PROCESSING_ERROR).HttpStatus(HttpStatus.OK)
            .build();
    LOG.error("Exception:", ex);
    return handleExceptionInternal(ex, errorObject, new HttpHeaders(), errorObject.getHttpStatus(),
        request);
  }

  @ExceptionHandler(value = {Error.class})
  protected ResponseEntity<Object> handleMainError(Error error, WebRequest request) {
    AppErrorObject errorObject =
        new AppErrorObject.Builder().appCode(ErrorCode.PROCESSING_ERROR).HttpStatus(HttpStatus.OK)
            .build();
    LOG.error("Java Error:", error);
    return handleExceptionInternal(new Exception(error), errorObject, new HttpHeaders(),
        errorObject.getHttpStatus(), request);
  }
}
