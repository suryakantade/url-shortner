package com.surya.crudproject.v1.service.impl;

import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.common.model.CrudProjectStatusCode;
import com.surya.crudproject.v1.entity.LocationTransactionEntity;
import com.surya.crudproject.v1.model.Location;
import com.surya.crudproject.v1.repository.DummyRepository;
import com.surya.crudproject.v1.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service("com.surya.crudproject.v1.service.impl.LocationServiceImpl")
public class LocationServiceImpl implements LocationService {

  @Autowired
  @Qualifier("com.surya.crudproject.v1.repository.DummyRepository")
  private DummyRepository dummyRepository;

  private Set<LocationTransactionEntity> locationTransactionEntities;
  private Long staticId = 0l;

  @Override
  public Boolean isLocationFree(Location location, Timestamp startTime, Timestamp endTime) {
    //is valid location

    Boolean isLocationFree = Boolean.FALSE;
    if (isValidLocation(location) && checkLocationFree(location, startTime, endTime)) {
      isLocationFree = Boolean.TRUE;
    }
    return isLocationFree;
  }

  @Override
  public void saveLocation(Location location, Timestamp startTime, Timestamp endTime) {
    staticId++;
    if (CollectionUtils.isEmpty(locationTransactionEntities)) {
      locationTransactionEntities = new HashSet<>();
    }
    locationTransactionEntities.add(
        LocationTransactionEntity.builder().id(staticId).locationId(location.getId())
            .startTime(startTime).endTime(endTime).build());
  }

  @Override
  public void makeLocationFree(Location location, Timestamp startTime, Timestamp endTime) {
    locationTransactionEntities.remove(
        LocationTransactionEntity.builder().id(staticId).locationId(location.getId())
            .startTime(startTime).endTime(endTime).build());
  }

  @Override
  public CrudProjectResponseObject<List<Location>> findAvailableLocations(Timestamp fromTime, Timestamp toTime) {
    List<Location> availableLocation = new ArrayList<>();
    for (Location location: getDummyLocation()){
      if(this.isLocationFree(location, fromTime, toTime)){
        availableLocation.add(location);
      }
    }
    return new CrudProjectResponseObject<>(CrudProjectStatusCode.SUCCESS, availableLocation);
  }

  private Boolean isValidLocation(Location location) {
    if (getDummyLocation().contains(location)) {
      return true;
    } else {
      return false;
    }
  }

  private List<Location> getDummyLocation() {
    return dummyRepository.getAllDummyLocation();
  }

  public Boolean checkLocationFree(Location location, Timestamp from, Timestamp to) {
    LocationTransactionEntity locationTransactionEntity =
        LocationTransactionEntity.builder().locationId(location.getId()).startTime(from).endTime(to)
            .build();
    return null != this.locationTransactionEntities && this.locationTransactionEntities
        .contains(locationTransactionEntity);
  }

}
