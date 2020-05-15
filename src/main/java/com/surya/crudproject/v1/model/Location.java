package com.surya.crudproject.v1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Location {
  private Long id;
  private String name;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(id, location.id) && Objects.equals(name, location.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
