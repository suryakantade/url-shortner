package com.shortener.urlshortener.v1.service.impl;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.v1.entity.TransactionAudit;
import com.shortener.urlshortener.v1.repository.TransactionAuditRepository;
import com.shortener.urlshortener.v1.service.TransactionAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Slf4j
@Service("com.shortener.urlshortener.v1.service.impl.TransactionAuditServiceImpl")
@EnableAsync
public class TransactionAuditServiceImpl implements TransactionAuditService {

  @Autowired
  @Qualifier("com.shortener.urlshortener.v1.repository.TransactionAuditRepository")
  private TransactionAuditRepository transactionAuditRepository;

  @Autowired
  @Qualifier("com.shortener.urlshortener.common.util.GenericUtility")
  private GenericUtility genericUtility;

  @Override
  @Async
  public void logTransaction(RequestContext context) {
    log.info("logging transaction for requestContext:{}", context);
    TransactionAudit transactionAudit = TransactionAudit.builder().requestContext(context)
        .createdOn(genericUtility.getCurrentTime()).build();
    transactionAuditRepository.save(transactionAudit);
  }

  @Override
  public UrlShortenerResponseObject<Page<TransactionAudit>> findLastDayLog(RequestContext context,
      Pageable pageable) {
    log.info("fetching log for context: {}, pageable: {} ", context, pageable);
    UrlShortenerResponseObject<Page<TransactionAudit>> responseObject =
        new UrlShortenerResponseObject<>(UrlShortenerStatusCode.SUCCESS);
    Page<TransactionAudit> transactionAudits = transactionAuditRepository.findAll(pageable);
    responseObject.setResponseObject(transactionAudits);
    return responseObject;
  }
}
