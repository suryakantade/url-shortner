package com.shortener.urlshortener.v1.controller;

import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
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

  @GetMapping(value = "/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void handleShortened(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, @PathVariable("token") String token) {
    log.info("recievied request to redirect to destination for token: {}", token);
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
      e.printStackTrace();
    }
  }
}
