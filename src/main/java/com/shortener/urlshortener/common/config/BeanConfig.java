package com.shortener.urlshortener.common.config;


import com.shortener.urlshortener.v1.enums.ServiceType;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import com.shortener.urlshortener.v1.service.impl.UrlShortenerPostgresqlServiceImpl;
import com.shortener.urlshortener.v1.service.impl.UrlShortenerRedisServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfig {

  @Bean(name ="urlShortenerServiceMap")
  public Map<ServiceType, UrlShortenerService> setUrlShortenerService(
      @Qualifier("com.shortener.urlshortener.v1.service.impl.UrlShortenerRedisServiceImpl")
      UrlShortenerRedisServiceImpl urlShortenerRedisServiceImpl,
      @Qualifier("com.shortener.urlshortener.v1.service.impl.UrlShortenerPostgresqlServiceImpl")
      UrlShortenerPostgresqlServiceImpl urlShortenerPostgresqlServiceImpl){
    Map<ServiceType, UrlShortenerService> shortenerServiceMap = new HashMap<>();
    shortenerServiceMap.put(ServiceType.REDIS, urlShortenerRedisServiceImpl);
    shortenerServiceMap.put(ServiceType.POSTGRESQL, urlShortenerPostgresqlServiceImpl);
    return shortenerServiceMap;
  }
}
