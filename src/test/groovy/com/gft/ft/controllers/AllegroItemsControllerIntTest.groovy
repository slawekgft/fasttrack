package com.gft.ft.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * Created by e-srwn on 2016-09-12.
 */
@ContextConfiguration(locations = "classpath:app-config-empty.xml")
@TestPropertySource(["/application.properties", "/application-dev.properties"])
class AllegroItemsControllerIntTest extends Specification {

    public static final String MANY_ITEMS_AVAILABLE_MSG = "Many items are available now"
    @Autowired
    AllegroItemsController allegroItemsController;

    def "Request item for given e-mail"() {
        expect:
        allegroItemsController.requestItem(category,keyword,email).indexOf("Request registered") > -1 == registeredItem

        where:
        category        | keyword   |   email           || registeredItem
        "Książ"         | "Rzym"    |   "user1@dom.com" || true
        "Książki"       | "wódz starożytność"    |   "user1@dom.com" || true
        "Film_Muzyka"   | "Mozart"    |   "user2@other.dom.com" || true
        "Komiks"        | "fantasy"   |   "user3@dom.com" || true
    }

    def "Request item of too much items"() {
        expect:
        allegroItemsController.requestItem(category,keyword,email).indexOf(MANY_ITEMS_AVAILABLE_MSG > -1 == tooManyItemsFound

        where:
        category        | keyword   |   email                   || tooManyItemsFound
        "Komputery"     | "TEST"    |   "user1@dom.com"         || false
        "Film"          | "Mozart"  |   "user1@other.dom.com"   || false
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
