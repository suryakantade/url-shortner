package com.shortener.urlshortener.v1.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.exception.UrlShortenerException;
import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.common.util.RedisClient;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("com.shortener.urlshortener.v1.service.impl.UrlShortenerRedisServiceImpl")
@Slf4j
public class UrlShortenerRedisServiceImpl implements UrlShortenerService {

  @Autowired
  @Qualifier("com.shortener.urlshortener.common.util.RedisClient")
  private RedisClient redisClient;

  private ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    objectMapper = new ObjectMapper();
  }

  //todo: check collision based on generated token

  @Override
  public UrlShortenerResponseObject<UrlShortenerModel> shortenUrl(RequestContext context,
      UrlShortenerModel urlShortenerModel) {
    log.info("shortening url for context: {} and urlShortenerModel: {}", context,
        urlShortenerModel);
    UrlShortenerResponseObject<UrlShortenerModel> responseObject =
        new UrlShortenerResponseObject<>(UrlShortenerStatusCode.DATA_VALIDATION_FAILED);
    if (urlShortenerModel.isValid()) {
      ShortUrl shortUrl =
          ShortUrl.builder().redirectedUrl(urlShortenerModel.getRedirectedUrl()).acccessCount(0)
              .expieryTime(new Timestamp(urlShortenerModel.getExpieryTime()))
              .isSingleAccess(urlShortenerModel.getIsSingleAccess()).clientId(context.getClientId())
              .token(CommonConstant.REDIS_KEY_PREFIX.concat(GenericUtility.getRandomToken(5)))
              .build();
      try {
        redisClient.hset(String.valueOf(context.getClientId()), shortUrl.getToken(),
            objectMapper.writeValueAsString(shortUrl));
        redisClient.set(shortUrl.getToken(), shortUrl.getRedirectedUrl());
      } catch (JsonProcessingException e) {
        log.error("exception occured while converting short url to string : {}", e);
        throw new UrlShortenerException(UrlShortenerStatusCode.PROCESSING_ERROR);
      }
      urlShortenerModel.setId(String.valueOf(shortUrl.getId()));
      urlShortenerModel.setToken(shortUrl.getToken());
      responseObject.setResponseObject(urlShortenerModel);
      responseObject.setStatus(UrlShortenerStatusCode.SUCCESS);
    } else {
      log.error("data validation failed for urlShortenerModel: {}", urlShortenerModel);
    }
    return responseObject;
  }

  @Override
  public UrlShortenerModel validateAndFetchShortenedDetails(String token) {
    log.info("fetching and validating shortened url info for token: {}", token);
    UrlShortenerModel urlShortenerModel = null;
    String shortUrl = redisClient.get(token);
    if (shortUrl != null) {
      urlShortenerModel =
          UrlShortenerModel.builder().redirectedUrl(shortUrl).build();
    } else {
      log.error("No short url found for token : {}", token);
    }
    return urlShortenerModel;
  }

  @Override
  public UrlShortenerResponseObject<List> findShortenedUrlList(RequestContext context) {
    UrlShortenerResponseObject<List> responseObject =
        new UrlShortenerResponseObject<>(UrlShortenerStatusCode.SUCCESS);
    Map<String, String> shortUrls = redisClient.hgetAll(String.valueOf(context.getClientId()));
    responseObject.setResponseObject(shortUrls.values().stream().map(e ->{
      try {
        return objectMapper.readValue(e,ShortUrl.class );
      } catch (IOException exception) {
        log.error("data validation failed for existing configuration: {}, exception :{}", e, exception);
        throw new UrlShortenerException(UrlShortenerStatusCode.PROCESSING_ERROR);
      }
    }).collect(Collectors.toList()));
    return responseObject;
  }

  @Override
  public UrlShortenerResponseObject<Boolean> deleteShortenedUrl(RequestContext context,
      String token) {
    log.info("deleting short url configured context: {}, token: {}", context,
        token);
    UrlShortenerResponseObject<Boolean> responseObject = new UrlShortenerResponseObject<>(UrlShortenerStatusCode.SUCCESS);
    if(StringUtils.isEmpty(token)){
      redisClient.del(token);
      String[] arr= {token};
      redisClient.hdel(String.valueOf(context.getClientId()), arr);
    }else{
      log.error("invalid token passed to be deleted");
      throw new UrlShortenerException(UrlShortenerStatusCode.DATA_VALIDATION_FAILED);
    }
    responseObject.setResponseObject(Boolean.TRUE);
    return responseObject;
  }

}