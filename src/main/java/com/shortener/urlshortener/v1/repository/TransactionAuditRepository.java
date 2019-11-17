package com.shortener.urlshortener.v1.repository;

import com.shortener.urlshortener.v1.entity.TransactionAudit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("com.shortener.urlshortener.v1.repository.TransactionAuditRepository")
public interface TransactionAuditRepository extends MongoRepository<TransactionAudit, String> {

}
