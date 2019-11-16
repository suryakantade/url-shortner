package com.shortener.urlshortener.v1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.common.util.RedisClient;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.repository.ShortUrlRepository;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service("com.shortener.urlshortener.v1.service.impl.UrlShortenerPostgresqlServiceImpl")
@Slf4j
public class UrlShortenerPostgresqlServiceImpl implements UrlShortenerService {


  @Autowired
  @Qualifier("com.shortener.urlshortener.v1.repository.ShortUrlRepository")
  private ShortUrlRepository shortUrlRepository;

  @Autowired
  @Qualifier("com.shortener.urlshortener.common.util.RedisClient")
  private RedisClient redisClient;

  private ObjectMapper objectMapper;

  @PostConstruct
  public void init() {
    objectMapper = new ObjectMapper();
  }

  //todo: check collision based on generated token and clientId

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
              .token(CommonConstant.POSTGRESQL_KEY_PREFIX.concat(GenericUtility.getRandomToken(5)))
              .build();
      shortUrl = shortUrlRepository.save(shortUrl);
      urlShortenerModel.setId(shortUrl.getId());
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
    Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByToken(token);
    ShortUrl shortUrl = shortUrlOptional.orElse(null);
    if (shortUrl != null && BooleanUtils.isNotTrue(shortUrl.getIsExpired())) {
      urlShortenerModel =
          UrlShortenerModel.builder().redirectedUrl(shortUrl.getRedirectedUrl()).build();
    } else {
      log.error("No short url found for token : {}", token);
    }
    return urlShortenerModel;
  }

  @Override
  public UrlShortenerResponseObject<List<ShortUrl>> findShortenedUrlList(RequestContext context) {
    UrlShortenerResponseObject<List<ShortUrl>> responseObject =
        new UrlShortenerResponseObject<>(UrlShortenerStatusCode.SUCCESS);
    List<ShortUrl> shortUrls = shortUrlRepository.findByClientId(context.getClientId());
    responseObject.setResponseObject(shortUrls);
    return responseObject;
  }

}
