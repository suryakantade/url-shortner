package com.surya.crudproject.v1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.surya.crudproject.v1.enums.Response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
public class User {

  private Long id;
  private String emailId;
  private String name;

  private Response response = Response.NOT_RESPONDED;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) || Objects.equals(emailId, user.emailId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, emailId);
  }
}
