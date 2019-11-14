package com.peoplehum.urlshortener.common.exception;

import com.nethum.errorhandling.exception.NethumBaseException;
import com.nethum.errorhandling.exception.error.AppErrorObject;
import com.peoplehum.urlshortener.common.model.NewProjectStatusCode;
import org.springframework.http.HttpStatus;

/**
 * Created by peoplehum on 22/11/18.
 */
public class NewProjectException extends NethumBaseException {

  private static final long serialVersionUID = 6696549368956277964L;

  /**
   * @param statusCode
   */
  public NewProjectException(NewProjectStatusCode statusCode)
  {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(HttpStatus.OK).build());
  }

  /**
   * @param statusCode
   * @param httpStatus
   */
  public NewProjectException(NewProjectStatusCode statusCode, HttpStatus httpStatus)
  {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(httpStatus).build());
  }

  public NewProjectException(NewProjectStatusCode code, Throwable throwable) {
    super(new AppErrorObject.Builder().throwable(throwable).HttpStatus(HttpStatus.OK).appCode(code)
        .build());
  }
}
