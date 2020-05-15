package com.surya.crudproject.v1.service.impl;


import com.surya.crudproject.common.exception.CrudProjectException;
import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.common.model.CrudProjectStatusCode;
import com.surya.crudproject.v1.enums.Response;
import com.surya.crudproject.v1.model.Event;
import com.surya.crudproject.v1.model.User;
import com.surya.crudproject.v1.repository.DummyRepository;
import com.surya.crudproject.v1.service.EventService;
import com.surya.crudproject.v1.service.LocationService;
import com.surya.crudproject.v1.service.UserService;
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
@Service("com.surya.crudproject.v1.service.impl.EventServiceImpl")
public class EventServiceImpl implements EventService {


  @Autowired
  @Qualifier("com.surya.crudproject.v1.repository.DummyRepository")
  private DummyRepository dummyRepository;

  @Autowired
  @Qualifier("com.surya.crudproject.v1.service.impl.LocationServiceImpl")
  private LocationService locationService;

  @Autowired
  @Qualifier("com.surya.crudproject.v1.service.impl.UserServiceImpl")
  private UserService userService;

  private Set<Event> events;
  private static Long enevtStaticId = 0l;

  @Override
  public CrudProjectResponseObject<Event> createEvent(Long userId, Event event) {
    log.info("creating calendar event : {}, by userId: {}", event, userId);
    CrudProjectResponseObject<Event> responseObject = new CrudProjectResponseObject<>();
    if (!userService.isValidUser(event.getOwner())) {
      throw new CrudProjectException(CrudProjectStatusCode.INVALID_USER);
    }
    if (!locationService
        .isLocationFree(event.getLocation(), event.getStartTime(), event.getEndTime())) {
      throw new CrudProjectException(CrudProjectStatusCode.INVALID_LOCATION_SCOPE);
    }
    saveEvent(event);
    locationService.saveLocation(event.getLocation(), event.getStartTime(), event.getEndTime());
    responseObject.setResponseObject(event);
    responseObject.setStatus(CrudProjectStatusCode.SUCCESS);
    return responseObject;
  }

  private void saveEvent(Event event) {
    enevtStaticId++;
    event.setId(enevtStaticId);
    if (CollectionUtils.isEmpty(events)) {
      events = new HashSet<>();
    }
    events.add(event);
  }

  @Override
  public CrudProjectResponseObject<Event> updateEvent(Long userId, Long eventId, Event event) {
    log.info("updating calendar event : {}, by userId: {}", event, userId);
    CrudProjectResponseObject<Event> responseObject = new CrudProjectResponseObject<>();
    if (events.contains(event)) {
      if (!userService.isValidUser(event.getOwner())) {
        throw new CrudProjectException(CrudProjectStatusCode.INVALID_USER);
      }
      if (!locationService
          .isLocationFree(event.getLocation(), event.getStartTime(), event.getEndTime())) {
        throw new CrudProjectException(CrudProjectStatusCode.INVALID_LOCATION_SCOPE);
      }
      Event existingEvent = findEventById(eventId);
      locationService.makeLocationFree(existingEvent.getLocation(), existingEvent.getStartTime(),
          existingEvent.getEndTime());
      locationService.saveLocation(event.getLocation(), event.getStartTime(), event.getEndTime());
      events.add(event);
    }
    responseObject.setResponseObject(event);
    responseObject.setStatus(CrudProjectStatusCode.SUCCESS);
    return responseObject;
  }

  @Override
  public CrudProjectResponseObject<Boolean> cancelEvent(Long UserId, Event event) {
    locationService.makeLocationFree(event.getLocation(), event.getStartTime(), event.getEndTime());
    return new CrudProjectResponseObject<>(CrudProjectStatusCode.SUCCESS, Boolean.TRUE);
  }

  @Override
  public CrudProjectResponseObject<List<Event>> findAllEvents(Long userId, Timestamp fromDate,
      Timestamp toDate) {
    List<Event> events = null;
    if (userService.isValidUser(User.builder().id(userId).build())) {
      events = new ArrayList<>();
      for (Event event : this.events) {
        if (event.getOwner().getId().equals(userId) || event.getUsers()
            .contains(User.builder().id(userId).build())) {
          if ((null == fromDate || event.getStartTime().before(fromDate)) && (null == toDate
              || event.getEndTime().after(toDate))) {
            events.add(event);
          }
        }
      }
    }
    return new CrudProjectResponseObject<>(CrudProjectStatusCode.SUCCESS, events);
  }

  @Override
  public CrudProjectResponseObject<Boolean> respond(Long userId, Long eventId, Response response) {
    Event existingEvent = this.findEventById(eventId);
    if (null != existingEvent && (existingEvent.getOwner().getId().equals(userId) || existingEvent
        .getUsers().contains(User.builder().id(userId).build()))) {
      User user =
          existingEvent.getUsers().stream().filter(u -> u.getId().equals(userId)).findFirst().get();
      user.setResponse(response);
    }
    return new CrudProjectResponseObject<>(CrudProjectStatusCode.SUCCESS, Boolean.TRUE);
  }

  @Override
  public CrudProjectResponseObject<Event> eventDetails(Long userId, Long eventId) {
    Event event = this.findEventById(eventId);
    return new CrudProjectResponseObject<>(CrudProjectStatusCode.SUCCESS, event);

  }

  private Event findEventById(Long eventId) {
    return events.stream().filter(e -> e.getId().equals(eventId)).findFirst().get();
  }
}
