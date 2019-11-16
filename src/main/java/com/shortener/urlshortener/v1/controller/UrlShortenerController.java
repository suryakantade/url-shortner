package com.shortener.urlshortener.v1.controller;


import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.shortener.urlshortener.common.constant.CommonConstant.VERSION_ONE;

@RestController
@CrossOrigin
@RequestMapping(value = VERSION_ONE)
@Slf4j
public class UrlShortenerController {

  @Autowired
  @Qualifier("com.shortener.urlshortener.common.util.GenericUtility")
  private GenericUtility genericUtility;

  @PostMapping(value = "/client/shorten/{serviceType}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UrlShortenerResponseObject> shorten(
      @PathVariable("serviceType") ServiceType serviceType,
      @RequestHeader("clientId") Integer clientId,
      @RequestHeader("authenticationKey") String authenticationKey,
      @RequestHeader("refreshKey") String refreshKey,
      @RequestBody UrlShortenerModel urlShortenerModel) {
    log.info("shortening url: {} for clientId: {}, authenticationKey: {} and refreshKey: {}",
        clientId, authenticationKey, refreshKey);
    RequestContext requestContext =
        RequestContext.builder().clientId(clientId).authenticationKey(authenticationKey)
            .refreshKey(refreshKey).build();
    validate(requestContext);
    UrlShortenerResponseObject<UrlShortenerModel> responseObject =
        genericUtility.findShortenerService(serviceType)
            .shortenUrl(requestContext, urlShortenerModel);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }


  @GetMapping(value = "/client/{clientId}/{serviceType}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UrlShortenerResponseObject> getShortenedList(
      @PathVariable("clientId") Integer clientId,
      @PathVariable("serviceType") ServiceType serviceType) {
    RequestContext requestContext = RequestContext.builder().clientId(clientId).build();
    UrlShortenerResponseObject<List<ShortUrl>> responseObject =
        genericUtility.findShortenerService(serviceType).findShortenedUrlList(requestContext);
    return new ResponseEntity<>(responseObject, responseObject.getStatusCode());
  }

  public void validate(RequestContext requestContext) {

  }

}
