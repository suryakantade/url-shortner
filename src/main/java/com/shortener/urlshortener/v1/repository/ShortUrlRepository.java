package com.shortener.urlshortener.v1.repository;

import com.shortener.urlshortener.v1.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("com.shortener.urlshortener.v1.repository.ShortUrlRepository")
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Integer> {

}

