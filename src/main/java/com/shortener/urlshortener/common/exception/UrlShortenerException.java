package com.shortener.urlshortener.common.exception;

import com.shortener.urlshortener.common.exception.error.AppErrorObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import org.springframework.http.HttpStatus;


public class UrlShortenerException extends BaseException {

  private static final long serialVersionUID = 6696549368956277964L;

  /**
   * @param statusCode
   */
  public UrlShortenerException(UrlShortenerStatusCode statusCode)
  {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(HttpStatus.OK).build());

  }

  /**
   * @param statusCode
   * @param httpStatus
   */
  public UrlShortenerException(UrlShortenerStatusCode statusCode, HttpStatus httpStatus)
  {
    super(new AppErrorObject.Builder().appCode(statusCode).HttpStatus(httpStatus).build());
  }

  public UrlShortenerException(UrlShortenerStatusCode code, Throwable throwable) {
    super(new AppErrorObject.Builder().throwable(throwable).HttpStatus(HttpStatus.OK).appCode(code)
        .build());
  }
}
