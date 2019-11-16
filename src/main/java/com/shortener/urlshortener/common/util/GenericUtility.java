package com.shortener.urlshortener.common.util;

import com.shortener.urlshortener.common.constant.CommonConstant;
import com.shortener.urlshortener.common.exception.UrlShortenerException;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("com.shortener.urlshortener.common.util.GenericUtility")
public class GenericUtility {

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
    ServiceType serviceType = null;
    if (token.startsWith(CommonConstant.REDIS_KEY_PREFIX)) {
      serviceType = ServiceType.REDIS;
    } else if (token.startsWith(CommonConstant.POSTGRESQL_KEY_PREFIX)) {
      serviceType = ServiceType.POSTGRESQL;
    }
    return this.findShortenerService(serviceType);
  }

  public UrlShortenerService findShortenerService(ServiceType serviceType) {
    if (null == serviceType) {
      throw new UrlShortenerException(UrlShortenerStatusCode.INVALID_SERVICE_TYPE);
    }
    return this.shortenerServiceMap.get(serviceType);
  }
}
