package com.gft.ft.allegrointerface

import org.springframework.beans.factory.annotation.Value
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

    def @Value("\${allegro.ws.webapi_key}") String apiKey
    def @Value("\${allegro.ws.country_id}") Integer countryId
    def @Shared ServiceService allegroService
    def @Shared ObjectFactory objectFactory

    def setupSpec() {
        allegroService = new ServiceService();
        objectFactory = new ObjectFactory();
    }

    def callMethod() {
        setup:
        def allegroPort = allegroService.getServicePort();
        def DoGetItemsListRequest doGetItemsListRequest = objectFactory.createDoGetItemsListRequest();

        doGetItemsListRequest.setWebapiKey = apiKey
        doGetItemsListRequest.setCountryId = countryId

        when:
        def itemsListResponse = allegroPort.doGetItemsList(doGetItemsListRequest)
        def categoryTreeTypes = itemsListResponse.getCategoriesList().getCategoriesTree().getItem()
        categoryTreeTypes.each { item -> println(item.categoryName + "(" + item.categoryId + ")")}

        then:
        categoryTreeTypes.size > 1
    }
}
