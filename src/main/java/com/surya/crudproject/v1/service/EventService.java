package com.surya.crudproject.v1.service;

import com.surya.crudproject.common.model.CrudProjectResponseObject;
import com.surya.crudproject.v1.enums.Response;
import com.surya.crudproject.v1.model.Event;

import java.sql.Timestamp;
import java.util.List;

public interface EventService {


  /**
   * @param userId
   * @param event
   *
   * @return
   */
  public CrudProjectResponseObject<Event> createEvent(Long userId, Event event);

  /**
   * @param userId
   * @param event
   *
   * @return
   */
  CrudProjectResponseObject<Event> updateEvent(Long userId, Long eventId, Event event);

  /**
   * @param userId
   * @param event
   *
   * @return
   */
  CrudProjectResponseObject<Boolean> cancelEvent(Long userId, Event event);


  /**
   *
   * @param userId
   * @param fromDate
   * @param toDate
   * @return
   */
  CrudProjectResponseObject<List<Event>> findAllEvents(Long userId, Timestamp fromDate,
      Timestamp toDate);

  /**
   *
   * @param userId
   * @param eventId
   * @param response
   * @return
   */
  CrudProjectResponseObject<Boolean> respond(Long userId, Long eventId, Response response);

  /**
   *
   * @param userId
   * @param eventId
   * @return
   */
  CrudProjectResponseObject<Event>  eventDetails(Long userId, Long eventId);
}
