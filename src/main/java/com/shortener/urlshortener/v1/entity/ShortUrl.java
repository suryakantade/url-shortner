package com.shortener.urlshortener.v1.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@Builder
@Entity(name = "SHORT_URL")
@Table
public class ShortUrl {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CLIENT_ID")
  private Long clientId;

  @Column(name = "REDIRECTED_URL")
  private String redirectedUrl;

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "IS_SINGLE_ACCESS")
  private boolean isSingleAccess;

  @Column(name = "EXPIERY_TIME")
  private Long expieryTime;

  @Column(name = "ACCESS_COUNT")
  private Integer acccessCount;

  @Column(name="IS_EXPIRED")
  private Boolean isExpired;
}
