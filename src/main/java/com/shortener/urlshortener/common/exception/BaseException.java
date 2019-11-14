package com.shortener.urlshortener.common.exception;

import com.shortener.urlshortener.common.exception.error.AppErrorObject;

public class BaseException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  private AppErrorObject customError;

  public BaseException(AppErrorObject customError) {
    super(customError.getThrowable());
    this.customError = customError;
  }

  public AppErrorObject getCustomError() {
    return customError;
  }

  public void setCustomError(AppErrorObject customError) {
    this.customError = customError;
  }
  
}
