package com.peoplehum.newproject.common.controller;

import com.nethum.custom.annotation.model.ApiMapping;
import com.peoplehum.newproject.common.service.ApiAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by peoplehum on 07/08/18.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/v1.0")
public class ApiAccessController {

  @Autowired
  @Qualifier("com.peoplehum.newproject.common.service.ApiAccessService")
  private ApiAccessService apiAccessService;

  @RequestMapping(value = "/api/access", method = RequestMethod.GET)
  public ResponseEntity<List<ApiMapping>> getApiAccesses() {
    List<ApiMapping> accesses = apiAccessService.getApiAccesses();
    return new ResponseEntity<>(accesses, HttpStatus.OK);
  }
}
