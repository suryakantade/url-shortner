package com.shortener.urlshortener.v1.repository;

import com.shortener.urlshortener.v1.entity.TransactionAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository("com.shortener.urlshortener.v1.repository.TransactionAuditRepository")
public interface TransactionAuditRepository extends MongoRepository<TransactionAudit, String> {

  Page<TransactionAudit> findAll(Pageable pageable);
}
