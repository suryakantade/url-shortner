package com.shortener.urlshortener.v1.entity;


import com.shortener.urlshortener.common.model.RequestContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@ToString(doNotUseGetters = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "transaction_audit")
public class TransactionAudit implements Serializable {

  private static final long serialVersionUID = -7510232579673082258L;

  private String id;

  private RequestContext requestContext;

  private Long createdOn;

}
