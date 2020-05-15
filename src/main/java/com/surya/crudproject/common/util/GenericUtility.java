package com.surya.crudproject.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component("com.surya.crudproject.common.util.GenericUtility")
public class GenericUtility {

  @Value("${short.url.host}")
  private String shortUrlHost;

  public Long getCurrentTime() {
    return Calendar.getInstance().getTimeInMillis();
  }

  public String generateShortUrl( String prefix, String token) {
    return prefix.concat(token);
  }

  public String combineHost(String token) {
    return shortUrlHost.concat(token);
  }
}
