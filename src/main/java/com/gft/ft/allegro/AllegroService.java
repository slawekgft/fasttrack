package com.gft.ft.allegro;

import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.ItemValidationException;
import com.gft.ft.daos.RequestsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class AllegroService {

    @Autowired
    private RequestsDAO requestsDAO;

    public void saveRequest(ItemRequest itemRequest) throws ItemValidationException{

    }
}
