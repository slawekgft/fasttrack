package com.gft.ft.requests

import com.gft.ft.commons.ItemRequest
import com.gft.ft.commons.ItemRequestStatus
import com.gft.ft.daos.RequestsDAO
import com.gft.ft.daos.RequestsDAOTest
import com.gft.ft.daos.entities.ItemRequestEntity
import org.junit.Ignore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager

import static com.gft.ft.tests.TestUtil.VALID_EMAIL
import static com.gft.ft.tests.TestUtil.createItemRequestEntity

/**
 * Created by e-srwn on 2016-09-16.
 */
@ContextConfiguration(locations = "classpath:app-config-empty.xml")
@TestPropertySource(["/application.properties", "/application-dev.properties"])
@Transactional
class RequestsServiceIntTest extends Specification {

    @Autowired
    private RequestsService requestsService

    @Autowired
    private RequestsDAO requestsDAO

    @Autowired
    private EntityManager entityManager;

    def "InvalidateRequests"() {
        given:
        finishedRequest()
        inProgressRequest()
        newProgressRequest()

        def validRequests = requestsService.getAllValidRequests()

        when:
        println("size="+validRequests.size())
        requestsService.invalidateRequests(new HashSet<ItemRequest>(validRequests))
        def validRequestsCountAfter = requestsService.getAllValidRequests().size()

        then:
        validRequestsCountAfter == 0
    }

    @Ignore
    def "InvalidateOldRequests"() {
        given:
        finishedRequest()
        inProgressRequest()
        newProgressRequest()

        def query = entityManager.createNativeQuery("UPDATE ITEM_REQUESTS SET create_Date = '2016-08-01 01:30:00.000-01:00' where status = 'IN_PROGRESS'")
        def updatedCount = query.executeUpdate()

        when:
        requestsService.invalidateOldRequests()
        def validRequestsCountAfter = requestsService.getAllValidRequests().size()

        then:
        updatedCount > 0
        validRequestsCountAfter == 0
    }

    def "ValidateNewRequests"() {
        given:
        finishedRequest()
        inProgressRequest()
        newProgressRequest()

        when:
        requestsService.validateNewRequests()
        def validRequestsCountAfter = requestsService.getAllValidRequests().size()

        then:
        validRequestsCountAfter == 2
    }

    private ItemRequestEntity finishedRequest() {
        return saveRequest(ItemRequestStatus.FINISHED)
    }

    private ItemRequestEntity inProgressRequest() {
        return saveRequest(ItemRequestStatus.IN_PROGRESS)
    }

    private ItemRequestEntity newProgressRequest() {
        return saveRequest(ItemRequestStatus.NEW)
    }

    private ItemRequestEntity saveRequest(ItemRequestStatus status) {
        ItemRequestEntity itemRequest =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(RequestsDAOTest.KW1)
                        .setStatus(status.name())
                        .setCategories("1,2").build();
        return requestsDAO.save(itemRequest)
    }

}
