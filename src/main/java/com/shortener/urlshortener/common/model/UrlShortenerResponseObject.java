package com.shortener.urlshortener.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;


@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UrlShortenerResponseObject<T> {
  private T responseObject;
  private UrlShortenerStatusCode status;

  @JsonIgnore
  private HttpStatus statusCode;

  public UrlShortenerResponseObject() {
    this.statusCode = HttpStatus.OK;
  }

  public UrlShortenerResponseObject(UrlShortenerStatusCode status) {
    this();
    this.status = status;
  }

  public UrlShortenerResponseObject(UrlShortenerStatusCode status, T responseObject) {
    this(status);
    this.responseObject = responseObject;
  }

}
