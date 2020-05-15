package com.surya.crudproject.v1.service;

import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.v1.model.Location;

import java.sql.Timestamp;
import java.util.List;

public interface LocationService {

  /**
   * @param location
   * @param startTime
   * @param endTime
   *
   * @return
   */
  Boolean isLocationFree(Location location, Timestamp startTime, Timestamp endTime);

  /**
   * @param location
   * @param startTime
   * @param endTime
   */
  void saveLocation(Location location, Timestamp startTime, Timestamp endTime);

  /**
   * @param location
   * @param timestamp
   * @param endTime
   */
  void makeLocationFree(Location location, Timestamp timestamp, Timestamp endTime);

  /**
   * @param fromTime
   * @param toTime
   *
   * @return
   */
  CrudProjectResponseObject<List<Location>> findAvailableLocations(Timestamp fromTime,
      Timestamp toTime);
}
