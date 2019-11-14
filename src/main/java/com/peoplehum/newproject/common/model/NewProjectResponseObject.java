package com.peoplehum.newproject.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Created by peoplehum on 21/11/18.
 */
@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class NewProjectResponseObject<T> {
  private T responseObject;
  private NewProjectStatusCode status;

  @JsonIgnore
  private HttpStatus statusCode;

  public NewProjectResponseObject() {
    this.statusCode = HttpStatus.OK;
  }

  public NewProjectResponseObject(NewProjectStatusCode status) {
    this();
    this.status = status;
  }

  public NewProjectResponseObject(NewProjectStatusCode status, T responseObject) {
    this(status);
    this.responseObject = responseObject;
  }

}
