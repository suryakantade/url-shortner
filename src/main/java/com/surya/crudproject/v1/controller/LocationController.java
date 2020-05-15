package com.surya.crudproject.v1.controller;


import com.surya.crudproject.common.constant.CommonConstant;
import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.common.model.RequestContext;
import com.surya.crudproject.v1.model.Location;
import com.surya.crudproject.v1.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = CommonConstant.VERSION_ONE)
@Slf4j
public class LocationController {

  @Autowired
  @Qualifier("com.surya.crudproject.v1.service.impl.LocationServiceImpl")
  private LocationService locationService;

  @GetMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> create(@RequestHeader("userId") Long userId,
      @RequestParam("fromDate") Timestamp fromDate, @RequestParam("toDate") Timestamp toDate) {
    CrudProjectResponseObject<List<Location>> responseObject =
        locationService.findAvailableLocations(fromDate, toDate);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  public void validate(RequestContext requestContext) {
    //validate existing client id
  }

}
