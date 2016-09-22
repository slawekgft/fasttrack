package com.gft.ft.allegro

import com.gft.ft.tests.TestUtil
import org.fest.assertions.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * Created by e-srwn on 2016-09-13.
 */
@ContextConfiguration(locations = "classpath:app-config-empty.xml")
@TestPropertySource(["/application.properties", "/application-dev.properties"])
class AllegroServiceIntTest extends Specification {

    @Autowired
    AllegroService allegroService

    def "FindItemsForCategoryAndKeyword"() {
        setup:
        def itemRequest = TestUtil.createItemRequest().setEmail("").setKeyword(key).setCategories(cats).build()

        expect:
        def items = allegroService.findItemsForCategoryAndKeyword(itemRequest)
        Assertions.assertThat(items).isNotNull()

        where:
        key        | cats
        "TEST"     | [2]
    }

    def "GetCategoriesNames"() {
        setup:

        expect:
        allegroService.getCategoriesNames(catetoriesIds).containsAll(catNames) == allFound

        where:
        catetoriesIds << [new HashSet<Integer>([1,2,3,4,7]),new HashSet<Integer>([1]),new HashSet<Integer>([1454,1378])]
        catNames << [['Muzyka','Komputery','Motoryzacja','Telefony i Akcesoria','Książki i Komiksy'],
                     ['Muzyka','Komputery'],
                     ['Odzież, Obuwie, Dodatki','Składanki']]
        allFound << [true,false,true]
    }

    def "FindCategoriesIds" () {
        setup:
        def foundCategoriesIds = allegroService.findCategoriesIds(filter)

        expect:
        foundCategoriesIds.containsAll(categoriesId);
        foundCategoriesIds.size() == categoriesId.size()

        where:
        filter << ["Telefony", "Komiksy","Książki i Komiksy"]
        categoriesId << [[47921,94139,15675,93710,5057,16331,8539,241,456,165,4],[250121,63857,79413,7],[7]]
    }
}
