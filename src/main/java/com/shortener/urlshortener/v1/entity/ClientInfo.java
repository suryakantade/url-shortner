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

@ToString(doNotUseGetters = true)
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Builder
//@Entity(name = "CLIENT_INFO")
//@Table
public class ClientInfo {

/*  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name ="NAME")
  private String name;

  @Column(name = "AUTHENTICATION_KEY")
  private String authenticationKey;

  @Column(name="EMAIL_ID")
  private String emailId;*/

}
