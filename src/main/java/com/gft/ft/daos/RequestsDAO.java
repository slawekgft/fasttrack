package com.gft.ft.daos;

import com.gft.ft.commons.ItemRequestStatus;
import com.gft.ft.daos.ent.ItemRequestEntity;
import com.gft.ft.daos.repos.ItemRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

import static com.gft.ft.commons.ItemRequestStatus.IN_PROGRESS;
import static com.gft.ft.commons.ItemRequestStatus.NEW;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class RequestsDAO {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    public ItemRequestEntity save(ItemRequestEntity itemRequestEntity) {
        return itemRequestRepository.save(itemRequestEntity);
    }

    public Collection<ItemRequestEntity> getValidItemsRequestsForUser(String email) {
        Collection<ItemRequestEntity> items = new ArrayList<>();
        items.addAll(itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, NEW.ordinal()));
        items.addAll(itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, IN_PROGRESS.ordinal()));

        return items;
    }

    public Collection<ItemRequestEntity> getClosedItemsRequestsForUser(String email) {
        return itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, ItemRequestStatus.FINISHED.ordinal());
    }

    public Collection<ItemRequestEntity> getFailedItemsRequestsForUser(String email) {
        return itemRequestRepository.findByEmailAndStatusOrderByCreateDateDesc(email, ItemRequestStatus.ERROR.ordinal());
    }

    public Collection<ItemRequestEntity> getValidItemsRequestsAllUsers() {
        return itemRequestRepository.findByStatusOrderByEmailAscCreateDateDesc(ItemRequestStatus.IN_PROGRESS.ordinal());
    }
}
