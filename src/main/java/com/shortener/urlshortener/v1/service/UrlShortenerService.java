package com.shortener.urlshortener.v1.service;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;

public interface UrlShortenerService {

  /**
   *
   * @param context
   * @param urlShortenerModel
   * @return
   */
  public UrlShortenerResponseObject<UrlShortenerModel> shortenUrl(RequestContext context,
      UrlShortenerModel urlShortenerModel);

  /**
   *
   * @param token
   * @return
   */
  public UrlShortenerModel validateAndFetchShortenedDetails(String token);
}
