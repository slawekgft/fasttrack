package com.gft.ft.daos;

import com.gft.ft.commons.ItemRequestStatus;
import com.gft.ft.daos.entities.ItemRequestEntity;
import com.gft.ft.daos.repos.ItemRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.gft.ft.commons.ItemRequestStatus.IN_PROGRESS;
import static com.gft.ft.commons.ItemRequestStatus.NEW;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Repository
public class RequestsDAO {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    public ItemRequestEntity save(ItemRequestEntity itemRequestEntity) {
        return itemRequestRepository.save(itemRequestEntity);
    }

    public Collection<ItemRequestEntity> getValidItemsRequestsForUser(String email) {
        Collection<ItemRequestEntity> items = new ArrayList<>();
        items.addAll(itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, NEW.name()));
        items.addAll(itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, IN_PROGRESS.name()));

        return items;
    }

    public Collection<ItemRequestEntity> getClosedItemsRequestsForUser(String email) {
        return itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, ItemRequestStatus.FINISHED.name());
    }

    public Collection<ItemRequestEntity> getValidItemsRequestsAllUsers() {
        return itemRequestRepository.findByStatusOrderByEmailAscCreateDateDesc(ItemRequestStatus.IN_PROGRESS.name());
    }

    public int invalidateRequests(List<Long> itemsIds) {
        return itemRequestRepository.invalidateRequests(itemsIds);
    }

    public int invalidateRequestsOlderThan(long days) {
        Instant cutDate = Instant.now().minus(days, ChronoUnit.DAYS);
        return itemRequestRepository.invalidateOldRequests(cutDate);
    }

    public int validateNewRequests() {
        return itemRequestRepository.validateNewRequests();
    }
}
