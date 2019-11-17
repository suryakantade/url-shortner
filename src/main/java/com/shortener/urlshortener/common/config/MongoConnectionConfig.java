package com.shortener.urlshortener.common.config;

import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories(basePackages = "com.shortener.urlshortener")
@Slf4j
public class MongoConnectionConfig {
  @Value("${db.mongo.uri}")
  private String uri;

  private MongoDbFactory mongoDbFactory() {
    try {
      return new SimpleMongoDbFactory(new MongoClientURI(uri));
    } catch (Exception e) {
      log.error("error while initializing mongo factory with uri {} :", uri, e);
    }
    return null;
  }

  @Bean(name = "mongoTemplate")
  public MongoTemplate getMongoTemplate() {
    return new MongoTemplate(mongoDbFactory());
  }
}
