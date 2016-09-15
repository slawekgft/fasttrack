package com.gft.ft.daos;

import com.gft.ft.commons.ItemRequestStatus;
import com.gft.ft.daos.ent.ItemRequestEntity;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.gft.ft.commons.ItemRequestStatus.ERROR;
import static com.gft.ft.commons.ItemRequestStatus.FINISHED;
import static com.gft.ft.commons.ItemRequestStatus.IN_PROGRESS;
import static com.gft.ft.tests.TestUtil.CAT_BOOKS;
import static com.gft.ft.tests.TestUtil.VALID_EMAIL;
import static com.gft.ft.tests.TestUtil.createItemRequestEntity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by e-srwn on 2016-09-09.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("/app-config-empty.xml")
@TestPropertySource({"/application.properties", "/application-dev.properties"})
@Transactional
public class RequestsDAOTest {

    public static final String KW1 = "Gotowanie";
    public static final String KW2 = "Sport";
    public static final String KW3 = "Motoryzacja";

    @Autowired
    private RequestsDAO requestsDAO;

    @Test
    public void shouldSave() throws Exception {
        //given
        ItemRequestEntity itemRequest =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW1)
                        .setStatus(IN_PROGRESS.ordinal())
                        .setCategories(CAT_BOOKS).build();

        // when
        final ItemRequestEntity savedRequest = requestsDAO.save(itemRequest);
        final Collection<ItemRequestEntity> requestsForUser = requestsDAO.getValidItemsRequestsForUser(VALID_EMAIL);

        // then
        assertThat(savedRequest.getId()).isNotNull();
        assertThat(savedRequest.getEmail()).isEqualTo(VALID_EMAIL);
        assertThat(requestsForUser).hasSize(1);
    }

    @Test
    public void shouldGetClosedItemsRequestsForUser() throws Exception {
        //given
        ItemRequestEntity itemRequest1 =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW1)
                        .setStatus(FINISHED.ordinal())
                        .setCategories(CAT_BOOKS).build();
        ItemRequestEntity itemRequest2 =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW2)
                        .setStatus(ERROR.ordinal())
                        .setCategories(CAT_BOOKS).build();
        requestsDAO.save(itemRequest1);
        requestsDAO.save(itemRequest2);

        // when
        final Collection<ItemRequestEntity> itemsRequestsForUser = requestsDAO.getClosedItemsRequestsForUser(VALID_EMAIL);

        // then
        assertThat(itemsRequestsForUser).hasSize(1);
    }

    @Test
    public void shouldGetAllItemsRequests() throws Exception {
        //given
        ItemRequestEntity itemRequest1 =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW1)
                        .setStatus(FINISHED.ordinal())
                        .setCategories(CAT_BOOKS).build();
        ItemRequestEntity itemRequest2 =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW2)
                        .setStatus(ERROR.ordinal())
                        .setCategories(CAT_BOOKS).build();
        ItemRequestEntity itemRequest3 =
                createItemRequestEntity()
                        .setEmail(VALID_EMAIL)
                        .setKeyword(KW3)
                        .setStatus(IN_PROGRESS.ordinal())
                        .setCategories(CAT_BOOKS).build();
        requestsDAO.save(itemRequest1);
        requestsDAO.save(itemRequest2);
        requestsDAO.save(itemRequest3);

        // when
        final Collection<ItemRequestEntity> allValidItemsRequests = requestsDAO.getValidItemsRequestsAllUsers();

        // then
        assertThat(allValidItemsRequests).hasSize(1);
        assertThat(allValidItemsRequests.iterator().next().getKeyword()).isEqualTo(KW3);
    }

}