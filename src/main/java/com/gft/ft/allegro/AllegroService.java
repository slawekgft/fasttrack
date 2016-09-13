package com.gft.ft.allegro;

import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.ItemValidationException;
import com.gft.ft.daos.RequestsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class AllegroService {

    private static final Logger log = LoggerFactory.getLogger(AllegroService.class);

    public Collection<Integer> findCategoriesIds(String categoryNameFilter) {
        return null;
    }

    public Collection<String> getCategoriesNames(Collection<Integer> categories) {
        return null;
    }
}
