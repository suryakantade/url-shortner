package com.shortener.urlshortener.v1.repository;

import com.shortener.urlshortener.v1.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("com.shortener.urlshortener.v1.repository.ShortUrlRepository")
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Integer> {

  /**
   *
   * @param token
   * @return
   */
  Optional<ShortUrl> findByToken(String token);

  /**
   *
   * @param clientId
   * @return
   */
  List<ShortUrl> findByClientId(Integer clientId);

  /**
   *
   * @param clientId
   * @param token
   */
  void deleteByClientIdAndToken(Integer clientId, String token);
}

