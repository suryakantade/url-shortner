package com.shortener.urlshortener.v1.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "SHORT_URL")
@Table
public class ShortUrl implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CLIENT_ID")
  private Integer clientId;

  @Column(name = "REDIRECTED_URL")
  private String redirectedUrl;

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "IS_SINGLE_ACCESS")
  private Boolean isSingleAccess;

  @Column(name = "EXPIERY_TIME")
  private Timestamp expieryTime;

  @Column(name = "ACCESS_COUNT")
  private Integer acccessCount;

  @Column(name="IS_EXPIRED")
  private Boolean isExpired;
}
