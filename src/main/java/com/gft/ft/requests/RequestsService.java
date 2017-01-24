package com.gft.ft.requests;

import com.gft.ft.allegro.AllegroOperationsService;
import com.gft.ft.commons.DBOperationProblemException;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.ItemRequestStatus;
import com.gft.ft.commons.TooMuchItemsFoundException;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.daos.RequestsDAO;
import com.gft.ft.daos.entities.ItemRequestEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Service
public class RequestsService {

    private static final Logger log = LoggerFactory.getLogger(RequestsService.class);

    @Value("${allegro.items_limit}")
    private Integer itemsLimit;

    @Autowired
    private RequestsDAO requestsDAO;

    @Autowired
    private AllegroOperationsService allegroOperationsService;

    @Transactional
    public void registerRequest(ItemRequest itemRequest) throws DBOperationProblemException, TooMuchItemsFoundException {
        log.debug("registerRequest " + itemRequest);
        Set<Item> availableItems = allegroOperationsService.findItemsForCategoryAndKeyword(itemRequest);
        if (CollectionUtils.size(availableItems) > itemsLimit) {
            throw new TooMuchItemsFoundException(availableItems);
        }
        try {
            ItemRequestEntity itemRequestEntity = mapRequest2Entity(itemRequest);
            requestsDAO.save(itemRequestEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DBOperationProblemException(e);
        }
    }

    public Collection<ItemRequest> getRequests(String email) {
        log.debug("getRequests for " + email);
        Collection<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.addAll(requestsDAO.getValidItemsRequestsForUser(email)
                .stream().map(itemRequestEntity -> mapEntity2ItemRequest(itemRequestEntity)).collect(toList()));
        itemRequests.addAll(requestsDAO.getClosedItemsRequestsForUser(email)
                .stream().map(itemRequestEntity -> mapEntity2ItemRequest(itemRequestEntity)).collect(toList()));

        return itemRequests;
    }

    public Collection<ItemRequest> getAllValidRequests() {
        log.debug("getAllValidRequests");
        return requestsDAO.getValidItemsRequestsAllUsers()
                .stream()
                .map(itemRequestEntity -> mapEntity2ItemRequest(itemRequestEntity))
                .collect(toList());
    }

    @Transactional
    public void invalidateRequests(Set<ItemRequest> requests) {
        requestsDAO.invalidateRequests(requests.stream()
                .mapToLong(ItemRequest::getId)
                .boxed()
                .collect(Collectors.toList()));
    }

    @Transactional
    public void invalidateOldRequests() {
        requestsDAO.invalidateRequestsOlderThan(30l);
    }

    @Transactional
    public void validateNewRequests() {
        requestsDAO.validateNewRequests();
    }

    private ItemRequestEntity mapRequest2Entity(ItemRequest itemRequest) {
        String categories = StringUtils.join(itemRequest.getCategories(), ",");
        ItemRequestEntity itemRequestEntity = new ItemRequestEntity(itemRequest.getEmail(), itemRequest.getKeyword(), categories);
        itemRequestEntity.setStatus(itemRequest.getStatus().name());

        return itemRequestEntity;
    }

    private ItemRequest mapEntity2ItemRequest(ItemRequestEntity itemRequestEntity) {
        Collection<Integer> categories =
                Arrays.stream(itemRequestEntity.getCategories().split(","))
                        .mapToInt(map2Int()).boxed().collect(toList());
        final ItemRequestStatus status = ItemRequestStatus.valueOf(itemRequestEntity.getStatus());
        ItemRequest itemRequest =
                new ItemRequest(itemRequestEntity.getId(),
                        itemRequestEntity.getEmail(),
                        itemRequestEntity.getKeyword(),
                        categories,
                        itemRequestEntity.getCreateDate(),
                        status);

        return itemRequest;
    }

}
