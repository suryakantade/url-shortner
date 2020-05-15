package com.surya.crudproject.v1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Objects;


@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
public class LocationTransactionEntity {
  private Long id;
  private Long locationId;
  private Timestamp startTime;
  private Timestamp endTime;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationTransactionEntity that = (LocationTransactionEntity) o;
    return Objects.equals(locationId, that.locationId) && startTime.before(that.startTime)
        && endTime.after(that.endTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId);
  }


}
