package com.gft.ft.daos.repos;

import com.gft.ft.daos.ent.ItemRequestEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by e-srwn on 2016-09-09.
 */
public interface ItemRequestRepository extends CrudRepository<ItemRequestEntity, Long> {
    Collection<ItemRequestEntity> findByEmailAndStatusOrderByCreateDateDesc(String email, int status);
}
