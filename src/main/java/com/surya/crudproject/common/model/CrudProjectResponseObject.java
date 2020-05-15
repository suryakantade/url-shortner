package com.surya.crudproject.common.model;

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
public class CrudProjectResponseObject<T> {
  private T responseObject;
  private CrudProjectStatusCode status;

  @JsonIgnore
  private HttpStatus statusCode;

  public CrudProjectResponseObject() {
    this.statusCode = HttpStatus.OK;
  }

  public CrudProjectResponseObject(CrudProjectStatusCode status) {
    this();
    this.status = status;
  }

  public CrudProjectResponseObject(CrudProjectStatusCode status, T responseObject) {
    this(status);
    this.responseObject = responseObject;
  }

}
