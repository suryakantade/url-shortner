package com.surya.crudproject.v1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

  private Long id;
  private Timestamp startTime;
  private Timestamp endTime;
  private Location location;
  private List<User> users;
  private User owner;
  private String title;


  public Boolean isValid() {
    return !isAnyNull(null);
  }

  public Boolean isAnyNull(Object... objects) {
    return Arrays.stream(objects).anyMatch(e -> e == null);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(id, event.id) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
