package com.surya.crudproject.common.exception;

import com.surya.crudproject.common.exception.error.AppErrorObject;
import com.surya.crudproject.common.model.CrudProjectStatusCode;
import org.springframework.http.HttpStatus;


public class CrudProjectException extends BaseException {

  private static final long serialVersionUID = 6696549368956277964L;

  /**
   * @param statusCode
   */
  public CrudProjectException(CrudProjectStatusCode statusCode) {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(HttpStatus.OK).build());

  }

  /**
   * @param statusCode
   * @param httpStatus
   */
  public CrudProjectException(CrudProjectStatusCode statusCode, HttpStatus httpStatus) {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(httpStatus).build());
  }

  public CrudProjectException(CrudProjectStatusCode code, Throwable throwable) {
    super(new AppErrorObject.Builder().throwable(throwable).HttpStatus(HttpStatus.OK).appCode(code)
        .build());
  }
}
