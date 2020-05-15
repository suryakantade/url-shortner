package com.surya.crudproject.v1.controller;


import com.surya.crudproject.common.constant.CommonConstant;
import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.common.model.RequestContext;
import com.surya.crudproject.v1.model.Event;
import com.surya.crudproject.v1.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = CommonConstant.VERSION_ONE)
@Slf4j
public class EventController {

  @Autowired
  @Qualifier("com.surya.crudproject.v1.service.impl.EventServiceImpl")
  private EventService eventService;

  @PostMapping(value = "/event/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> create(@RequestHeader("userId") Long userId,
      @RequestBody Event event) {
    CrudProjectResponseObject<Event> responseObject = eventService.createEvent(userId, event);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  @PutMapping(value = "/event/{eventId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> update(@PathVariable("eventId") Long eventId,
      @RequestHeader("userId") Long userId, @RequestBody Event event) {
    CrudProjectResponseObject<Event> responseObject =
        eventService.updateEvent(userId, eventId, event);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  @DeleteMapping(value = "/event/{eventId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> delete(@PathVariable("eventId") Long eventId,
      @RequestHeader("userId") Long userId, @RequestBody Event event) {
    CrudProjectResponseObject<Boolean> responseObject = eventService.cancelEvent(userId, event);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  @GetMapping(value = "/event/{eventId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CrudProjectResponseObject> getEvent(@PathVariable("eventId") Long eventId,
      @RequestHeader("userId") Long userId) {
    CrudProjectResponseObject<Event> responseObject = eventService.eventDetails(userId, eventId);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  public void validate(RequestContext requestContext) {
    //validate existing client id
  }

}
