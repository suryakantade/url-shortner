package com.shortener.urlshortener.common.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.enums.WorkflowType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
public class RequestContext implements Serializable {
  private Integer clientId;
  private String authenticationKey;
  private String refreshKey;
  private String token;
  private ServiceType serviceType;
  private WorkflowType workflowType;
}
