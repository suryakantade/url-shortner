package com.shortener.urlshortener;

import com.shortener.urlshortener.common.model.RequestContext;
import com.shortener.urlshortener.common.model.UrlShortenerResponseObject;
import com.shortener.urlshortener.common.model.UrlShortenerStatusCode;
import com.shortener.urlshortener.v1.entity.ShortUrl;
import com.shortener.urlshortener.v1.model.UrlShortenerModel;
import com.shortener.urlshortener.v1.repository.ShortUrlRepository;
import com.shortener.urlshortener.v1.service.impl.UrlShortenerPostgresqlServiceImpl;
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

@RunWith(MockitoJUnitRunner.class)
public class PostgresServiceImplTest {

  @InjectMocks
  UrlShortenerPostgresqlServiceImpl urlShortenerPostgresqlService;

  @Mock
  private ShortUrlRepository shortUrlRepository;

  private InOrder inOrder;
  RequestContext requestContext = null;
  UrlShortenerModel urlShortenerModel = null;
  ShortUrl shortUrl = null;
  String token = "raYzdD";
  Integer clientId = 1;

  @Before
  public void init() {
    inOrder = Mockito.inOrder(shortUrlRepository);

    requestContext =
        RequestContext.builder().clientId(clientId).refreshKey("abc").authenticationKey("123").build();

    urlShortenerModel = UrlShortenerModel.builder().redirectedUrl(
        "https://www.quora.com/unanswered/What-are-some-of-the-funniest-questions-asked-on-Quora")
        .clientId(1).expieryTime(1573768584L).build();

    shortUrl = ShortUrl.builder().redirectedUrl(urlShortenerModel.getRedirectedUrl())
        .clientId(Integer.valueOf(urlShortenerModel.getClientId())).build();

  }

  @After
  public void tearDown() {
    inOrder.verifyNoMoreInteractions();
  }


  @Test
  public void shortenUrl() {
    Mockito.when(shortUrlRepository.save(Mockito.any(ShortUrl.class))).thenReturn(shortUrl);
    UrlShortenerResponseObject<UrlShortenerModel> response =
        urlShortenerPostgresqlService.shortenUrl(requestContext, urlShortenerModel);
    inOrder.verify(shortUrlRepository).save(Mockito.any(ShortUrl.class));
    inOrder.verifyNoMoreInteractions();
    assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());
  }


  @Test
  public void validateAndFetchShortenedDetails() {
    Mockito.when(shortUrlRepository.findByToken(token)).thenReturn(Optional.of(shortUrl));
    UrlShortenerModel response =
        urlShortenerPostgresqlService.validateAndFetchShortenedDetails(token);
    inOrder.verify(shortUrlRepository).findByToken(token);
    inOrder.verifyNoMoreInteractions();
    assertNotNull(response.getRedirectedUrl());
  }

  @Test
  public void findShortenedUrlList() {
    Mockito.when(shortUrlRepository.findByClientId(clientId)).thenReturn(Arrays.asList(shortUrl));
    UrlShortenerResponseObject<List<ShortUrl>> response =
        urlShortenerPostgresqlService.findShortenedUrlList(requestContext);
    inOrder.verify(shortUrlRepository).findByClientId(clientId);
    inOrder.verifyNoMoreInteractions();
    assertNotNull(response.getResponseObject());
    assertTrue(CollectionUtils.isNotEmpty(response.getResponseObject()));
    assertEquals(UrlShortenerStatusCode.SUCCESS, response.getStatus());
  }
}
