package com.shortener.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortener.urlshortener.common.exception.UrlShortenerException;
import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.common.util.RedisClient;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.service.impl.UrlShortenerRedisServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RedisServiceImplTest {


  @InjectMocks
  UrlShortenerRedisServiceImpl urlShortenerRedisService;

  @Mock
  private RedisClient redisClient;

  @Mock
  ObjectMapper objectMapper;

  ObjectMapper mapper;
  private InOrder inOrder;
  RequestContext requestContext = null;
  UrlShortenerModel urlShortenerModel = null;
  ShortUrl shortUrl = null;
  String token = "raYzdD";
  Integer clientId = 1;
  Long savedId = 1L;
  String STATUS_CODE_SUCCESS = "STATUS_CODE_SUCCESS";

  @Before
  public void init() {
    inOrder = Mockito.inOrder(redisClient, objectMapper);

    requestContext =
        RequestContext.builder().clientId(clientId).refreshKey("abc").authenticationKey("123")
            .build();

    urlShortenerModel = UrlShortenerModel.builder().redirectedUrl(
        "https://www.quora.com/unanswered/What-are-some-of-the-funniest-questions-asked-on-Quora")
        .clientId(1).expieryTime(1573768584L).build();

    shortUrl = ShortUrl.builder().redirectedUrl(urlShortenerModel.getRedirectedUrl())
        .clientId(Integer.valueOf(urlShortenerModel.getClientId())).build();

    mapper = new ObjectMapper();

  }

  @After
  public void tearDown() {
    inOrder.verifyNoMoreInteractions();
  }


  @Test
  public void shortenUrl() {

      Mockito.when(redisClient.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
          .thenReturn(savedId);
      Mockito.when(redisClient.set(Mockito.any(String.class), Mockito.anyString()))
          .thenReturn(STATUS_CODE_SUCCESS);
      UrlShortenerResponseObject<UrlShortenerModel> response =
          urlShortenerRedisService.shortenUrl(requestContext, urlShortenerModel);
      inOrder.verify(redisClient)
          .hset(Mockito.anyString(), Mockito.anyString(), Mockito.any());
      inOrder.verify(redisClient).set(Mockito.any(String.class), Mockito.anyString());
      inOrder.verifyNoMoreInteractions();
      assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());

  }


  @Test
  public void validateAndFetchShortenedDetails() {
    Mockito.when(redisClient.get(token)).thenReturn(urlShortenerModel.getRedirectedUrl());
    UrlShortenerModel response = urlShortenerRedisService.validateAndFetchShortenedDetails(token);
    inOrder.verify(redisClient).get(token);
    inOrder.verifyNoMoreInteractions();
    assertNotNull(response.getRedirectedUrl());
  }

  @Test
  public void findShortenedUrlList() {
    Map<String, String> map = new HashMap<>();
    try {
      map.put(shortUrl.getToken(), mapper.writeValueAsString(shortUrl));
      Mockito.when(redisClient.hgetAll(String.valueOf(clientId))).thenReturn(map);

      Mockito.when(objectMapper.readValue(mapper.writeValueAsString(shortUrl), ShortUrl.class))
          .thenReturn(shortUrl);
      UrlShortenerResponseObject<List<ShortUrl>> response =
          urlShortenerRedisService.findShortenedUrlList(requestContext);
      inOrder.verify(redisClient).hgetAll(String.valueOf(clientId));
      inOrder.verify(objectMapper).readValue(mapper.writeValueAsString(shortUrl), ShortUrl.class);

      inOrder.verifyNoMoreInteractions();
      assertNotNull(response.getResponseObject());
      assertTrue(CollectionUtils.isNotEmpty(response.getResponseObject()));
      assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());
    } catch (Exception ex) {
      throw new UrlShortenerException(UrlShortenerStatusCode.PROCESSING_ERROR);
    }
  }
}
