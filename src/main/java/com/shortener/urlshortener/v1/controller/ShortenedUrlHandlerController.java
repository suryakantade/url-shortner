package com.shortener.urlshortener.v1.controller;

import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.exception.UrlShortenerException;
import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.v1.enums.WorkflowType;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.service.TransactionAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping
@Slf4j
public class ShortenedUrlHandlerController {


  @Autowired
  @Qualifier("com.shortener.urlshortener.common.util.GenericUtility")
  private GenericUtility genericUtility;

  @Autowired
  @Qualifier("com.shortener.urlshortener.v1.service.impl.TransactionAuditServiceImpl")
  private TransactionAuditService transactionAuditService;

  @GetMapping(value = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void handleShortened(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, @PathVariable("token") String token) {
    log.info("recievied request to redirect to destination for token: {}", token);
    RequestContext requestContext =
        RequestContext.builder().token(token).serviceType(genericUtility.getServiceType(token))
            .workflowType(WorkflowType.EXPAND).build();
    UrlShortenerModel urlShortenerModel =
        genericUtility.findShortenerService(token).validateAndFetchShortenedDetails(token);
    if (null != urlShortenerModel) {
      try {
        httpServletResponse.sendRedirect(urlShortenerModel.getRedirectedUrl());
      } catch (IOException e) {
        log.error("exception occured while redirecting");
      }
    }
    try {
      httpServletResponse.sendRedirect(CommonConstant.ERROR_PAGE_STRING);
    } catch (IOException e) {
      log.error("exception occured while redirecting to default error page: {}",
          CommonConstant.ERROR_PAGE_STRING);
      throw new UrlShortenerException(UrlShortenerStatusCode.PROCESSING_ERROR);
    }
  }

  public void log(RequestContext context) {
    transactionAuditService.logTransaction(context);
  }
}
