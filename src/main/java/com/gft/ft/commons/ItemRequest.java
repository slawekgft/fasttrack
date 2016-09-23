package com.gft.ft.commons;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by e-srwn on 2016-09-07.
 */
public class ItemRequest {
    private Long id;
    private String email;
    private String keyword;
    private Collection<Integer> categories = new ArrayList<>();
    private ItemRequestStatus status = ItemRequestStatus.NEW;
    private Instant createDate = Instant.now();

    public ItemRequest(String email, String keyword, Collection<Integer> categories) {
        this.email = email;
        this.keyword = keyword;
        this.categories = categories;
    }

    public ItemRequest(Long id, String email, String keyword, Collection<Integer> categories, Instant createDate, ItemRequestStatus status) {
        this(email, keyword, categories);
        this.id = id;
        this.status = status;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
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

    public Instant getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemRequest that = (ItemRequest) o;

        if (!email.equals(that.email)) return false;
        if (!keyword.equals(that.keyword)) return false;
        if (!createDate.equals(that.createDate)) return false;

        return categories.equals(that.categories);

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + keyword.hashCode();
        result = 31 * result + categories.hashCode();
        result = 31 * result + createDate.hashCode();

        return result;
    }
}
