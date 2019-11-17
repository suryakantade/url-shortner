package com.shortener.urlshortener;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.v1.entity.ShortUrlMongo;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.repository.ShortUrlMongoRepository;
import com.shortener.urlshortener.v1.service.impl.UrlShortenerMongoServiceImpl;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.Silent.class)
public class MongoServiceImplTest {


  @InjectMocks
  UrlShortenerMongoServiceImpl urlShortenerPostgresqlService;

  @Mock
  private ShortUrlMongoRepository shortUrlMongoRepository;

  private InOrder inOrder;
  RequestContext requestContext = null;
  UrlShortenerModel urlShortenerModel = null;
  ShortUrlMongo shortUrl = null;
  String token = "raYzdD";
  Integer clientId = 1;

  @Before
  public void init() {
    inOrder = Mockito.inOrder(shortUrlMongoRepository);

    requestContext =
        RequestContext.builder().clientId(clientId).refreshKey("abc").authenticationKey("123")
            .build();

    urlShortenerModel = UrlShortenerModel.builder().redirectedUrl(
        "https://www.quora.com/unanswered/What-are-some-of-the-funniest-questions-asked-on-Quora")
        .clientId(1).expieryTime(1573768584L).build();

    shortUrl = ShortUrlMongo.builder().redirectedUrl(urlShortenerModel.getRedirectedUrl())
        .clientId(Integer.valueOf(urlShortenerModel.getClientId())).build();

  }

  @After
  public void tearDown() {
    inOrder.verifyNoMoreInteractions();
  }


  @Test
  public void shortenUrl() {
    Mockito.when(shortUrlMongoRepository.save(Mockito.any(ShortUrlMongo.class)))
        .thenReturn(shortUrl);
    UrlShortenerResponseObject<UrlShortenerModel> response =
        urlShortenerPostgresqlService.shortenUrl(requestContext, urlShortenerModel);
    inOrder.verify(shortUrlMongoRepository).save(Mockito.any(ShortUrlMongo.class));
    inOrder.verifyNoMoreInteractions();
    assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());
  }


  @Test
  public void validateAndFetchShortenedDetails() {
    Mockito.when(shortUrlMongoRepository.findByToken(token)).thenReturn(Optional.of(shortUrl));
    UrlShortenerModel response =
        urlShortenerPostgresqlService.validateAndFetchShortenedDetails(token);
    inOrder.verify(shortUrlMongoRepository).findByToken(token);
    inOrder.verifyNoMoreInteractions();
    assertNotNull(response.getRedirectedUrl());
  }

  @Test
  public void findShortenedUrlList() {
    Mockito.when(shortUrlMongoRepository.findByClientId(clientId))
        .thenReturn(Arrays.asList(shortUrl));
    UrlShortenerResponseObject<List> response =
        urlShortenerPostgresqlService.findShortenedUrlList(requestContext);
    inOrder.verify(shortUrlMongoRepository).findByClientId(clientId);
    inOrder.verifyNoMoreInteractions();
    assertNotNull(response.getResponseObject());
    assertTrue(CollectionUtils.isNotEmpty(response.getResponseObject()));
    assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());
  }
}
