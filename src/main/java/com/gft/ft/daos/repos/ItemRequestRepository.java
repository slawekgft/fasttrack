package com.gft.ft.daos.repos;

import com.gft.ft.daos.entities.ItemRequestEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by e-srwn on 2016-09-09.
 */
public interface ItemRequestRepository extends CrudRepository<ItemRequestEntity, Long> {
    Collection<ItemRequestEntity> findByEmailAndStatusOrderByCreateDateDesc(String email, int status);
    Collection<ItemRequestEntity> findByStatusOrderByEmailAscCreateDateDesc(int status);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 3 where ir.id in ?1")
    int invalidateRequests(List<Long> itemsIds);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 3 where ir.createDate < ?1")
    int invalidateOldRequests(LocalDateTime cutDate);

    @Modifying
    @Query("update ItemRequestEntity ir set ir.status = 1 where ir.status = 0")
    int validateNewRequests();
}
