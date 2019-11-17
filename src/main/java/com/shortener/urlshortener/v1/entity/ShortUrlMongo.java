package com.shortener.urlshortener.v1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;


@ToString(doNotUseGetters = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "SHORT_URL")
public class ShortUrlMongo implements Serializable {

  @Id
  private String id;

  private Integer clientId;

  private String redirectedUrl;

  private String token;

  private Boolean isSingleAccess;

  private Timestamp expieryTime;

  private Integer acccessCount;

  private Boolean isExpired;

}
