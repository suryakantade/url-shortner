package com.shortener.urlshortener.v1.service;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.v1.entity.TransactionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionAuditService {
  /**
   * @param context
   */
  void logTransaction(RequestContext context);

  /**
   * @param context
   * @param pageable
   *
   * @return
   */
  public UrlShortenerResponseObject<Page<TransactionAudit>> findLastDayLog(RequestContext context,
      Pageable pageable);
}
