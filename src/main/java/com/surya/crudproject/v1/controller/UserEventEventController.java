package com.surya.crudproject.v1.controller;


import com.surya.crudproject.common.constant.CommonConstant;
import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.common.model.RequestContext;
import com.surya.crudproject.v1.enums.Response;
import com.surya.crudproject.v1.model.Event;
import com.surya.crudproject.v1.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = CommonConstant.VERSION_ONE)
@Slf4j
public class UserEventEventController {

  @Autowired
  @Qualifier("com.surya.crudproject.v1.service.impl.EventServiceImpl")
  private EventService eventService;

  @GetMapping(value = "/user/{userId}/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> find(@PathVariable("userId") Long userId,
      @RequestParam("fromDate") Timestamp fromDate, @RequestParam("toDate") Timestamp toDate) {
    CrudProjectResponseObject<List<Event>> responseObject =
        eventService.findAllEvents(userId, fromDate, toDate);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  @GetMapping(value = "/user/{userId}/event/{eventId}/respond",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> respond(@PathVariable("userId") Long userId,
      @PathVariable("eventId") Long eventId, @RequestParam("response") Response response) {
    CrudProjectResponseObject<Boolean> responseObject =
        eventService.respond(userId, eventId, response);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }


  public void validate(RequestContext requestContext) {
    //validate existing client id
  }

}
