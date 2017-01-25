package com.gft.ft.processing;

import com.gft.ft.allegro.AllegroOperationsService;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.messages.MessagesService;
import com.gft.ft.requests.RequestsService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Service
public class ItemsService {

    private static Logger log = LoggerFactory.getLogger(ItemsService.class);

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private AllegroOperationsService allegroOperationsService;

    @Autowired
    private MessagesService messagesService;

    @Scheduled(cron = "10 */1 * * * MON-FRI")
    public void periodicalCheck() {
        log.debug("periodicalCheck");
        final Collection<ItemRequest> allValidRequests = requestsService.getAllValidRequests();

        final Map<String, List<ItemRequest>> reqestsByEmail = allValidRequests.stream().collect(Collectors.groupingBy(ItemRequest::getEmail));
        reqestsByEmail.values().stream()
                .filter(CollectionUtils::isNotEmpty)
                .forEach(checkAndProcessUserRequests());
        requestsService.invalidateOldRequests();
        requestsService.validateNewRequests();
    }

    private Consumer<? super List<ItemRequest>> checkAndProcessUserRequests() {
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
                return itemRequest -> {
                    final Set<Item> itemsForCategoryAndKeyword = allegroOperationsService.findItemsForCategoryAndKeyword(itemRequest);
                    if (isNotEmpty(itemsForCategoryAndKeyword)) {
                        processedRequests.add(itemRequest);
                    }
                    userItems.addAll(itemsForCategoryAndKeyword);
                };
            }

        };
    }
}
