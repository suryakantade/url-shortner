package com.shortener.urlshortener.v1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ToString(doNotUseGetters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlShortenerModel {
  //making it string so for supporting mongo id
  private String id;
  private Integer clientId;
  private String redirectedUrl;
  private String token;
  private Boolean isSingleAccess;
  private Long expieryTime;

  public Boolean isValid(){
    return !isAnyNull(this.getRedirectedUrl());
  }
  public Boolean isAnyNull(Object ...objects){
    return Arrays.stream(objects).anyMatch(e->e==null);
  }
}
