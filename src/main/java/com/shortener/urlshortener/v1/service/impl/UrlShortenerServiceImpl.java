package com.shortener.urlshortener.v1.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.GenericUtility;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.repository.ShortUrlRepository;
import com.shortener.urlshortener.v1.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("com.shortener.urlshortener.common.v1.service.impl.UrlShortenerServiceImpl")
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService {


  @Autowired
  @Qualifier("com.shortener.urlshortener.v1.repository.ShortUrlRepository")
  private ShortUrlRepository shortUrlRepository;

  //todo: check collision based on generated token and clientId

  @Override
  public UrlShortenerResponseObject<UrlShortenerModel> shortenUrl(RequestContext context,
      UrlShortenerModel urlShortenerModel) {
    log.info("shortening url for context: {} and urlShortenerModel: {}", context,
        urlShortenerModel);
    UrlShortenerResponseObject<UrlShortenerModel> responseObject =
        new UrlShortenerResponseObject<>(UrlShortenerStatusCode.SUCCESS);
    ShortUrl shortUrl =
        ShortUrl.builder().redirectedUrl(urlShortenerModel.getRedirectedUrl()).acccessCount(0)
            .expieryTime(urlShortenerModel.getExpieryTime())
            .isSingleAccess(urlShortenerModel.getIsSingleAccess()).clientId(context.getClientId())
            .token(GenericUtility.getRandomToken(5)).build();
    shortUrl = shortUrlRepository.save(shortUrl);
    urlShortenerModel.setId(shortUrl.getId());
    urlShortenerModel.setToken(shortUrl.getToken());
    responseObject.setResponseObject(urlShortenerModel);
    return responseObject;
  }

  public static void main(String[] args) throws JsonProcessingException {
    UrlShortenerModel urlShortenerModel = UrlShortenerModel.builder().redirectedUrl(
        "https://www.quora.com/unanswered/What-are-some-of-the-funniest-questions-asked-on-Quora")
        .expieryTime(1573768584L).build();
    System.out.println(new ObjectMapper().writeValueAsString(urlShortenerModel));
  }
}
