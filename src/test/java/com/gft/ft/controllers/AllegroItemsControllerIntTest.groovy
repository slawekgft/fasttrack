package com.gft.ft.controllers

import com.gft.ft.tests.TestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by e-srwn on 2016-09-12.
 */
@ContextConfiguration(locations = "classpath:app-config-rest.xml")
class AllegroItemsControllerIntTest extends Specification {

    @Autowired
    AllegroItemsController allegroItemsController;



    def "Request item for given e-mail"() {
        expect:
        allegroItemsController.requestItem(category,keyword,email).indexOf("Request registered") > -1 == poprawnaOdpowiedz

        where:
        category        | keyword   |   email           || poprawnaOdpowiedz
        "Książ"         | "Rzym"    |   "user1@dom.com" || true
        "Książki"       | "wódz starożytność"    |   "user1@dom.com" || true
        "Film_Muzyka"   | "Mozart"    |   "user2@other.dom.com" || true
        "Komiks"        | "fantasy"   |   "user3@dom.com" || true
    }

    def "Check items for e-mail"() {
        setup:
        if(!noRequests)
            allegroItemsController.requestItem("książki","Homer","user1@dom.com")

        expect:
        noRequests == allegroItemsController.checkRequests(email).indexOf("No requests found.") > -1

        where:
        email               || noRequests
        "unknown@dom.com"   || true
        "user1@dom.com"     || false
    }
}
