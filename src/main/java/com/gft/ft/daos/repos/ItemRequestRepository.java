package com.gft.ft.daos.repos;

import com.gft.ft.daos.entities.ItemRequestEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

/**
 * Created by e-srwn on 2016-09-09.
 */
public interface ItemRequestRepository extends CrudRepository<ItemRequestEntity, Long> {
    Collection<ItemRequestEntity> findByEmailAndStatusOrderByCreateDateDesc(String email, String status);
    Collection<ItemRequestEntity> findByStatusOrderByEmailAscCreateDateDesc(String status);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 'FINISHED' where ir.id in ?1")
    int invalidateRequests(List<Long> itemsIds);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 'FINISHED' where ir.createDate < ?1")
    int invalidateOldRequests(Instant cutDate);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 'IN_PROGRESS' where ir.status = 'NEW'")
    int validateNewRequests();
}
