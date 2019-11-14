package com.peoplehum.urlshortener.common.service;

import com.nethum.custom.annotation.model.ApiMapping;
import com.nethum.custom.annotation.operation.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by peoplehum on 07/08/18.
 */
@Service("com.peoplehum.newproject.common.service.ApiAccessService")
@Slf4j
public class ApiAccessService {

  @Autowired
  ListableBeanFactory listableBeanFactory;

  private Action action;

  @PostConstruct
  public void setup() {
    action = new Action();
  }

  public List<ApiMapping> getApiAccesses() {
    log.info("getting accesses and entitlements for API's of one2one ListableBeanFactory : {}",
        listableBeanFactory);
    return action.getApiAccess(listableBeanFactory);
  }
}
