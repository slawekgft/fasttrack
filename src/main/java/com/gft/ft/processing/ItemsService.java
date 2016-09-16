package com.gft.ft.processing;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.messages.MessagesService;
import com.gft.ft.requests.RequestsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class ItemsService {

    private static Logger log = LoggerFactory.getLogger(ItemsService.class);

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private AllegroService allegroService;

    @Autowired
    private MessagesService messagesService;

    @Scheduled(cron = "10 */1 * * * MON-FRI")
    public void periodicalCheck() {
        log.debug("periodicalCheck");
        final Collection<ItemRequest> allValidRequests = requestsService.getAllValidRequests();

        final Map<String, List<ItemRequest>> reqestsByEmail = allValidRequests.stream().collect(Collectors.groupingBy(ItemRequest::getEmail));
        reqestsByEmail.values().stream().filter(notEmpty()).forEach(checkUserRequests());
    }

    private Predicate<List<ItemRequest>> notEmpty() {
        return new Predicate<List<ItemRequest>>() {
            @Override
            public boolean test(List<ItemRequest> itemRequests) {
                return isNotEmpty(itemRequests);
            }
        };
    }

    private Consumer<? super List<ItemRequest>> checkUserRequests() {
        Set<ItemRequest> processedRequests = new HashSet<>();
        return new Consumer<List<ItemRequest>>() {
            @Override
            public void accept(List<ItemRequest> itemRequests) {
                final Set<Item> userItems = new HashSet<>();
                final String email = itemRequests.get(0).getEmail();
                itemRequests.forEach(findAvailableItems(userItems));
                if (isNotEmpty(userItems)) {
                    messagesService.mailItemAvailable(email, userItems);
                    requestsService.invalidateRequests(processedRequests);
                }
            }

            private Consumer<ItemRequest> findAvailableItems(final Set<Item> userItems) {
                return new Consumer<ItemRequest>() {
                    @Override
                    public void accept(ItemRequest itemRequest) {
                        final Set<Item> itemsForCategoryAndKeyword = allegroService.findItemsForCategoryAndKeyword(itemRequest);
                        if (isNotEmpty(itemsForCategoryAndKeyword)) {
                            processedRequests.add(itemRequest);
                        }
                        userItems.addAll(itemsForCategoryAndKeyword);
                    }
                };
            }

        };
    }
}
