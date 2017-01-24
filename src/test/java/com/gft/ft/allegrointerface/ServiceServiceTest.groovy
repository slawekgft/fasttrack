package com.gft.ft.allegrointerface

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by e-srwn on 2016-09-13.
 */
@ContextConfiguration(locations = "classpath:app-config-empty.xml")
@TestPropertySource(["/application.properties", "/application-dev.properties"])
class ServiceServiceTest extends Specification {

    def @Shared AllegroWebService allegroService
    def @Autowired ObjectFactory objectFactory

    def setupSpec() {
        allegroService = new AllegroWebService();
    }

    def callMethod() {
        setup:
        def allegroPort = allegroService.getServicePort();
        def DoGetItemsListRequest doGetItemsListRequest = objectFactory.createDoGetItemsListRequest();

        when:
        def itemsListResponse = allegroPort.doGetItemsList(doGetItemsListRequest)
        def categoryTreeTypes = itemsListResponse.getCategoriesList().getCategoriesTree().getItem()
        categoryTreeTypes.each { item -> println(item.categoryName + "(" + item.categoryId + ")")}

        then:
        categoryTreeTypes.size > 1
    }
}
