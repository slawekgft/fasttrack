package com.gft.ft.requests;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.ItemRequestStatus;
import com.gft.ft.commons.ItemValidationException;
import com.gft.ft.daos.RequestsDAO;
import com.gft.ft.daos.ent.ItemRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class RequestsService {

    private static final Logger log = LoggerFactory.getLogger(RequestsService.class);

    @Autowired
    private RequestsDAO requestsDAO;

    public void saveRequest(ItemRequest itemRequest) {
        log.debug("saveRequest " + itemRequest);
        ItemRequestEntity itemRequestEntity = mapRequest2Entity(itemRequest);

        requestsDAO.save(itemRequestEntity);
    }

    private ItemRequestEntity mapRequest2Entity(ItemRequest itemRequest) {
        String categories = StringUtils.join(itemRequest.getCategories(), ",");
        ItemRequestEntity itemRequestEntity = new ItemRequestEntity(itemRequest.getEmail(), itemRequest.getKeyword(), categories);
        itemRequestEntity.setStatus(itemRequest.getStatus().ordinal());

        return itemRequestEntity;
    }

    public Collection<ItemRequest> getRequests(String email) {
        log.debug("getRequests for " + email);
        Collection<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.addAll(requestsDAO.getValidItemsRequestsForUser(email).stream().map(mapEntity2ItemRequest()).collect(toList()));
        itemRequests.addAll(requestsDAO.getClosedItemsRequestsForUser(email).stream().map(mapEntity2ItemRequest()).collect(toList()));

        return itemRequests;
    }

    private Function<ItemRequestEntity, ItemRequest> mapEntity2ItemRequest() {
        return new Function<ItemRequestEntity, ItemRequest>() {
            @Override
            public ItemRequest apply(ItemRequestEntity itemRequestEntity) {
                Collection<Integer> categories =
                        Arrays.stream(itemRequestEntity.getCategories().split(","))
                                .mapToInt(map2Int()).boxed().collect(toList());
                final ItemRequestStatus status = ItemRequestStatus.valueOf(itemRequestEntity.getStatus());
                ItemRequest itemRequest =
                        new ItemRequest(itemRequestEntity.getEmail(),
                                        itemRequestEntity.getKeyword(),
                                        categories,
                                        status);

                return itemRequest;
            }
        };
    }

    private ToIntFunction<String> map2Int() {
        return new ToIntFunction<String>() {
            @Override
            public int applyAsInt(String value) {
                return Integer.parseInt(value);
            }
        };
    }
}
