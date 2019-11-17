package com.shortener.urlshortener.v1.controller;


import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.v1.entity.TransactionAudit;
import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.enums.WorkflowType;
import com.shortener.urlshortener.v1.service.TransactionAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shortener.urlshortener.common.constant.CommonConstant.VERSION_ONE;

@RestController
@CrossOrigin
@RequestMapping(value = VERSION_ONE)
@Slf4j
public class TransactionController {

  @Autowired
  @Qualifier("com.shortener.urlshortener.v1.service.impl.TransactionAuditServiceImpl")
  private TransactionAuditService transactionAuditService;

  @GetMapping(value = "/client/{clientId}/transactions",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UrlShortenerResponseObject> getShortenedList(
      @PathVariable("clientId") Integer clientId,
      Pageable pageable) {
    RequestContext requestContext =
        RequestContext.builder().clientId(clientId)
            .workflowType(WorkflowType.API_AUDIT).build();
    UrlShortenerResponseObject<Page<TransactionAudit>> responseObject =
        transactionAuditService.findLastDayLog(requestContext, pageable);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }
}
