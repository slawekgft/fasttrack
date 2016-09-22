package com.gft.ft.tests;

import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.daos.entities.ItemRequestEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by e-srwn on 2016-09-09.
 */
public class TestUtil {

    public static final String REQUEST_REGISTERED_MSG = "Request registered";

    public static final String VALID_EMAIL = "test@gft.test.com";
    public static final String VALID_OTHER_EMAIL = "test2@gft.test.com";
    public static final String CAT_BOOKS_COM = "Książki i Komiksy";
    public static final String CAT_BOOKS = "Książki";

    private TestUtil() {
    }

    public static final ItemBuilder createItem() {
        return new ItemBuilder();
    }

    public static class ItemBuilder {
        private Item item;

        ItemBuilder() {
            this.item = new Item();
        }

        public ItemBuilder setId(Long id) {
            this.item.setId(id);
            return this;
        }

        public ItemBuilder setName(String name) {
            this.item.setName(name);
            return this;
        }

        public Item build() {
            return item;
        }
    }

    public static final ItemRequestBuilder createItemRequest() {
        return new ItemRequestBuilder();
    }

    public static class ItemRequestBuilder {
        private String keyword;
        private String email;
        private Collection<Integer> categories;

        ItemRequestBuilder() {
        }

        public ItemRequestBuilder setEmail(final String email) {
            this.email = email;
            return this;
        }

        public ItemRequestBuilder setKeyword(final String keyword) {
            this.keyword = keyword;
            return this;
        }

        public ItemRequestBuilder setCategories(final Collection<Integer> categories) {
            this.categories = categories;
            return this;
        }

        public ItemRequestBuilder setCategories(final Integer ... categories) {
            if(null != categories) {
                this.categories = Arrays.asList(categories);
            }
            return this;
        }

        public ItemRequest build() {
            return new ItemRequest(email, keyword, categories);
        }
    }

    public static final ItemRequestEntityBuilder createItemRequestEntity() {
        return new ItemRequestEntityBuilder();
    }

    public static class ItemRequestEntityBuilder {
        private ItemRequestEntity itemRequestEntity;

        ItemRequestEntityBuilder() {
            this.itemRequestEntity = new ItemRequestEntity(null, null, null);
        }

        public ItemRequestEntityBuilder setId(Long id) {
            itemRequestEntity.setId(id);
            return this;
        }

        public ItemRequestEntityBuilder setEmail(String email) {
            itemRequestEntity.setEmail(email);
            return this;
        }

        public ItemRequestEntityBuilder setKeyword(String keyword) {
            itemRequestEntity.setKeyword(keyword);
            return this;
        }

        public ItemRequestEntityBuilder setCategories(String categories) {
            itemRequestEntity.setCategories(categories);
            return this;
        }

        public ItemRequestEntityBuilder setCreateDate(LocalDateTime createDate) {
            itemRequestEntity.setCreateDate(createDate);
            return this;
        }

        public ItemRequestEntityBuilder setStatus(int status) {
            itemRequestEntity.setStatus(status);
            return this;
        }

        public ItemRequestEntity build() {
            return itemRequestEntity;
        }
    }

    public static final boolean registeredItem(String info) {
        return info.indexOf(REQUEST_REGISTERED_MSG) > -1;
    }

    public static Set<Item> createItemsSet(int count) {
        Set<Item> set = new HashSet<>();
        for(int i=0; i<count; i++) {
            final long id = (long) (i + 31) * set.hashCode();
            set.add(createItem().setId(id).setName("item " + id).build());
        }
        return set;
    }

}
