package com.shortener.urlshortener.v1.service;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;

import java.util.List;

public interface UrlShortenerService {

  /**
   *
   * @param context
   * @param urlShortenerModel
   * @return
   */
  UrlShortenerResponseObject<UrlShortenerModel> shortenUrl(RequestContext context,
      UrlShortenerModel urlShortenerModel);

  /**
   *
   * @param token
   * @return
   */
  UrlShortenerModel validateAndFetchShortenedDetails(String token);

  /**
   *
   * @param context
   * @return
   */
  public UrlShortenerResponseObject<List> findShortenedUrlList(RequestContext context);

  /**
   *
   * @param context
   * @param token
   * @return
   */
  UrlShortenerResponseObject<Boolean> deleteShortenedUrl(RequestContext context,
      String token);

}
