package com.gft.ft.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by e-srwn on 2016-09-06.
 */
@RestController
public class AllegroItemsController {

    private static Logger log = LoggerFactory.getLogger(AllegroItemsController.class);

    //hasło: KontoTestowe1,
    //uż: slawek_test
    //Klucz Allegro WebAPI: 	5a71c4b2

    /*
    Klucz Sandbox WebAPI: 	sc5f646c
    Użytkownik: 	slawek_test
    Hasło:  c5f646c5380559b2
     */

    public AllegroItemsController() {
        log.debug(">>>>>> AllegroItemsController");
    }

    @RequestMapping(value = "/find", produces = MediaType.TEXT_HTML_VALUE)
    public String findItem(@RequestParam(value = "name") String itemName) {
        return "<h2>no items</h2>";
    }
}
