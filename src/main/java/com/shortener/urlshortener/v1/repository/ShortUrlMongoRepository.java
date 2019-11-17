package com.shortener.urlshortener.v1.repository;

import com.shortener.urlshortener.v1.entity.ShortUrlMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("com.shortener.urlshortener.v1.repository.ShortUrlMongoRepository")
public interface ShortUrlMongoRepository extends MongoRepository<ShortUrlMongo, String> {
  /**
   *
   * @param token
   * @return
   */
  Optional<ShortUrlMongo> findByToken(String token);

  /**
   *
   * @param clientId
   * @return
   */
  List<ShortUrlMongo> findByClientId(Integer clientId);

  /**
   *
   * @param clientId
   * @param token
   */
  void deleteByClientIdAndToken(Integer clientId, String token);
}
