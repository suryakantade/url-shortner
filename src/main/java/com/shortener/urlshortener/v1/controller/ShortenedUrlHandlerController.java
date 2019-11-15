package com.shortener.urlshortener.v1.controller;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.shortener.urlshortener.common.constant.CommonConstant.VERSION_ONE;

@RestController
@CrossOrigin
@RequestMapping(value = VERSION_ONE)
@Slf4j
public class ShortenedUrlHandlerController {

  @Autowired
  @Qualifier("com.shortener.urlshortener.common.v1.service.impl.UrlShortenerServiceImpl")
  private UrlShortenerService urlShortenerService;

  @GetMapping(value = "/{token}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void handleShortened(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      @PathVariable("token") String token) {
    log.info("recievied request to redirect to destination for token: {}", token);

    UrlShortenerModel urlShortenerModel =
        urlShortenerService.validateAndFetchShortenedDetails(token);
    if(null != urlShortenerModel){
      try {
        httpServletResponse.sendRedirect(urlShortenerModel.getRedirectedUrl());
      } catch (IOException e) {
        log.error("exception occured while redirecting");
      }
    }
    try {
      httpServletResponse.sendRedirect(
            "https://github.com/suryakantade/url-shortner/blob/master/src/main/java/com/shortener/urlshortener/v1/controller/UrlShortenerController.java");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
