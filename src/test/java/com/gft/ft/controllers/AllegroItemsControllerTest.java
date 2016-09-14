package com.gft.ft.controllers;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.DBOperationProblemException;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.TooMuchItemsFoundException;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.requests.RequestsService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.*;

import static com.gft.ft.tests.TestUtil.*;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;

/**
 * Created by e-srwn on 2016-09-09.
 */
@RunWith(MockitoJUnitRunner.class)
public class AllegroItemsControllerTest {

    private static final String REQUEST_PROBLEM_MSG = "Request problem";
    public static final List<Integer> DEFAULT_CATEGORIES = Arrays.asList(1, 7, 9);

    @InjectMocks
    private AllegroItemsController allegroItemsController = new AllegroItemsController();

    @Mock
    private RequestsService requestsService;

    @Mock
    private AllegroService allegroService;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        given(messageSource.getMessage(AllegroItemsController.WEB_ITEMS_REQUEST_REGISTERED_MSG, null, Locale.US)).willReturn(REQUEST_REGISTERED_MSG);
        given(messageSource.getMessage(AllegroItemsController.WEB_ITEMS_REQUEST_PROBLEM_MSG, null, Locale.US)).willReturn(REQUEST_PROBLEM_MSG);
        given(allegroService.findCategoriesIds(anyString())).willReturn(DEFAULT_CATEGORIES);
        given(allegroService.findItemsForCategoryAndKeyword(any(ItemRequest.class))).willReturn(Collections.emptySet());
    }

    @Test
    public void shouldRequestBookItemFullCategoryOneWordEmailOk() throws Exception {
        //given

        //when
        String info = allegroItemsController.requestItem(CAT_BOOKS_COM, "droga", VALID_EMAIL);

        //then
        assertThat(registeredItem(info)).isTrue();
        verify(requestsService).registerRequest(any(ItemRequest.class));
    }

    @Test
    public void shouldRequestBookItemPartCategoryOneWordEmailOk() throws Exception {
        //given

        //when
        String info = allegroItemsController.requestItem(CAT_BOOKS, "droga", VALID_EMAIL);

        //then
        assertThat(registeredItem(info)).isTrue();
        verify(requestsService).registerRequest(any(ItemRequest.class));
    }

    @Test
    public void shouldRequestBookItemPartCategoryTwoWordsEmailOk() throws Exception {
        //given

        //when
        String info = allegroItemsController.requestItem(CAT_BOOKS, "długa droga", VALID_EMAIL);

        //then
        assertThat(registeredItem(info)).isTrue();
        verify(requestsService).registerRequest(any(ItemRequest.class));
    }

    @Test
    public void shouldRequestItemWithTechnicalProblem() throws Exception {
        //given
        Mockito.doThrow(DBOperationProblemException.class).when(requestsService).registerRequest(any(ItemRequest.class));

        //when
        String info = allegroItemsController.requestItem(CAT_BOOKS, "długa droga", "any@any.com");

        //then
        assertThat(technicalProblem(info)).isTrue();
    }

    @Test
    public void shouldCheckItemRequestsExists() throws Exception {
        //given
        final List<ItemRequest> requests = new ArrayList<>();
        requests.add(createItemRequest().setEmail(VALID_EMAIL).setKeyword("misie").setCategories(asList(1,2,4)).build());
        requests.add(createItemRequest().setEmail(VALID_EMAIL).setKeyword("rycerz").setCategories(asList(1,2,4)).build());
        given(requestsService.getRequests(VALID_EMAIL)).willReturn(requests);
        given(allegroService.getCategoriesNames(anySet())).willReturn(Arrays.asList("kat1", "kat2"));

        //when
        final String requestsListInfo = allegroItemsController.checkRequests(VALID_EMAIL);

        //then
        assertThat(numberOfLines(requestsListInfo)).isEqualTo(2);
        verify(requestsService).getRequests(VALID_EMAIL);
    }

    @Test(expected = TooMuchItemsFoundException.class)
    public void shouldRequestBookItemTooMuchItemsFound() throws Exception {
        //given
        given(allegroService.findItemsForCategoryAndKeyword(any(ItemRequest.class))).willReturn(set(20));

        //when
        allegroItemsController.requestItem(CAT_BOOKS_COM, "droga", VALID_EMAIL);

        //then
    }

    private Set<Item> set(int count) {
        Set<Item> set = new HashSet<>();
        for(int i=0; i<count; i++) {
            set.add(createItem().setId((long) i).build());
        }
        return set;
    }

    private int numberOfLines(String requestsListInfo) {
        return StringUtils.countMatches(requestsListInfo, "<li>");
    }

    private boolean technicalProblem(String info) {
        return info.indexOf(REQUEST_PROBLEM_MSG) > -1;
    }

}