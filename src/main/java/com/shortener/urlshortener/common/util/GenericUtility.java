package com.shortener.urlshortener.common.util;

import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.exception.UrlShortenerException;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Map;

@Component("com.shortener.urlshortener.common.util.GenericUtility")
public class GenericUtility {

  @Value("${short.url.host}")
  private String shortUrlHost;


  @Resource(name = "urlShortenerServiceMap")
  private Map<ServiceType, UrlShortenerService> shortenerServiceMap;

  private static final String ALPHA_NUMERIC_STRING =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  public static String getRandomToken(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
      builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
  }

  public UrlShortenerService findShortenerService(String token) {

    return this.findShortenerService(getServiceType(token));
  }

  public ServiceType getServiceType(String token) {
    ServiceType serviceType = null;
    if (token.startsWith(CommonConstant.REDIS_KEY_PREFIX)) {
      serviceType = ServiceType.REDIS;
    } else if (token.startsWith(CommonConstant.POSTGRESQL_KEY_PREFIX)) {
      serviceType = ServiceType.POSTGRESQL;
    } else if (token.startsWith(CommonConstant.MONGO_KEY_PREFIX)) {
      serviceType = ServiceType.MONGO;
    }
    return serviceType;
  }

  public UrlShortenerService findShortenerService(ServiceType serviceType) {
    if (null == serviceType) {
      throw new UrlShortenerException(UrlShortenerStatusCode.INVALID_SERVICE_TYPE);
    }
    return this.shortenerServiceMap.get(serviceType);
  }

  public Long getCurrentTime() {
    return Calendar.getInstance().getTimeInMillis();
  }

  public String generateShortUrl( String prefix, String token) {
    return shortUrlHost.concat(prefix).concat(token);
  }
}
