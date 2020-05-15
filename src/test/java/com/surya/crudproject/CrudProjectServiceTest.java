package com.surya.crudproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surya.crudproject.common.model.RequestContext;
import com.surya.crudproject.common.util.GenericUtility;
import com.surya.crudproject.v1.model.CrudProjectModel;
import com.surya.crudproject.v1.repository.DummyRepository;
import com.surya.crudproject.v1.service.impl.EventServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CrudProjectServiceTest {

  @InjectMocks
  private EventServiceImpl crudService;

  @Mock
  private DummyRepository dummyRepository;

  @Mock
  private GenericUtility genericUtility;

  @Mock
  ObjectMapper objectMapper;

  private InOrder inOrder;
  RequestContext requestContext = null;
  CrudProjectModel crudProjectModel = null;
  Integer clientId = 1;


  @Before
  public void init() {
    inOrder = Mockito.inOrder(dummyRepository);

    requestContext =
        RequestContext.builder().clientId(clientId).refreshKey("abc").authenticationKey("123").build();

    crudProjectModel = CrudProjectModel.builder().redirectedUrl(
        "https://www.quora.com/unanswered/What-are-some-of-the-funniest-questions-asked-on-Quora")
        .clientId(1).expieryTime(1573768584L).build();



  }

  @After
  public void tearDown() {
    inOrder.verifyNoMoreInteractions();
  }


  @Test
  public void shortenUrl() {
    /*Mockito.when(dummyRepository.save(Mockito.any(ShortUrl.class))).thenReturn(shortUrl);
    Mockito.when(genericUtility.combineHost(Mockito.anyString())).thenReturn(combinedHost);
    CrudProjectResponseObject<CrudProjectModel> response =
        urlShortenerPostgresqlService.shortenUrl(requestContext, crudProjectModel);
    inOrder.verify(shortUrlRepository).save(Mockito.any(CrudProjectEntity.class));
    inOrder.verifyNoMoreInteractions();
    Assert.assertEquals(CrudProjectStatusCode.SUCCESS, response.getStatus());*/
  }

}
