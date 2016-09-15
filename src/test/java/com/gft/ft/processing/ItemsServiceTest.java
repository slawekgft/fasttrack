package com.gft.ft.processing;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.messages.MessagesService;
import com.gft.ft.requests.RequestsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static com.gft.ft.tests.TestUtil.*;
import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by e-srwn on 2016-09-14.
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemsServiceTest {

    public static final int EMPTY = 0;

    @InjectMocks
    private ItemsService itemsService = new ItemsService();

    @Mock
    private RequestsService requestsService;

    @Mock
    private AllegroService allegroService;

    @Mock
    private MessagesService messagesService;

    @Test
    public void shouldDoPeriodicalCheckWithNoActiveRequests() throws Exception {
        //given
        given(requestsService.getAllValidRequests()).willReturn(emptyList());

        // when
        itemsService.periodicalCheck();

        // then
        verify(messagesService, times(0)).mailItemAvailable(anyString(), anySet());
        verify(requestsService, times(0)).invalidateRequests(anySet());
    }

    @Test
    public void shouldDoPeriodicalCheckWithNoItemsForActiveRequests() throws Exception {
        //given
        final ArrayList<ItemRequest> requests = new ArrayList<>();
        requests.add(createItemRequest().setCategories(1, 2, 3).setKeyword("Laptop").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(10, 20, 30).setKeyword("Card").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(10, 30).setKeyword("Herbert").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(100).setKeyword("Java").setEmail(VALID_OTHER_EMAIL).build());

        given(requestsService.getAllValidRequests()).willReturn(requests);
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(0))).willReturn(set(EMPTY));
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(1))).willReturn(set(EMPTY));
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(2))).willReturn(set(EMPTY));
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(3))).willReturn(set(EMPTY));

        // when
        itemsService.periodicalCheck();

        // then
        verify(messagesService, times(0)).mailItemAvailable(anyString(), anySet());
        verify(requestsService, times(0)).invalidateRequests(anySet());
    }

    @Test
    public void shouldDoPeriodicalCheckWithActiveRequests() throws Exception {
        //given
        final ArrayList<ItemRequest> requests = new ArrayList<>();
        requests.add(createItemRequest().setCategories(1, 2, 3).setKeyword("Laptop").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(10, 20, 30).setKeyword("Card").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(10, 30).setKeyword("Herbert").setEmail(VALID_EMAIL).build());
        requests.add(createItemRequest().setCategories(100).setKeyword("Java").setEmail(VALID_OTHER_EMAIL).build());

        given(requestsService.getAllValidRequests()).willReturn(requests);
        final Set<Item> userSet0 = set(EMPTY), userSet1 = set(2), userSet2 = set(1), userAllSets = set(EMPTY), otherUserSet = set(1);
        userAllSets.addAll(userSet0);
        userAllSets.addAll(userSet1);
        userAllSets.addAll(userSet2);
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(0))).willReturn(userSet0);
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(1))).willReturn(userSet1);
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(2))).willReturn(userSet2);
        given(allegroService.findItemsForCategoryAndKeyword(requests.get(3))).willReturn(otherUserSet);

        // when
        itemsService.periodicalCheck();

        // then
        verify(messagesService).mailItemAvailable(VALID_EMAIL, userAllSets);
        verify(messagesService).mailItemAvailable(VALID_OTHER_EMAIL, otherUserSet);
        verify(requestsService, times(2)).invalidateRequests(anySet());
    }
}