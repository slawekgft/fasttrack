package com.gft.ft.commons;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by e-srwn on 2016-09-07.
 */
public class ItemRequest {
    private String email;
    private String keyword;
    private Collection<Integer> categories = new ArrayList<>();
    private ItemRequestStatus status = ItemRequestStatus.NEW;

    public ItemRequest(String email, String keyword, Collection<Integer> categories) {
        this.email = email;
        this.keyword = keyword;
        this.categories = categories;
    }

    public ItemRequest(String email, String keyword, Collection<Integer> categories, ItemRequestStatus status) {
        this(email, keyword, categories);
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getKeyword() {
        return keyword;
    }

    public Collection<Integer> getCategories() {
        return categories;
    }

    public ItemRequestStatus getStatus() {
        return status;
    }
}
