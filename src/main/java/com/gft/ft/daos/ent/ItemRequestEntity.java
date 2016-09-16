package com.gft.ft.daos.ent;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by e-srwn on 2016-09-09.
 */
@Entity
@Table(name = "ITEM_REQUESTS")
public class ItemRequestEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String keyword;
    @Column(nullable = false)
    private String categories;
    @Column(nullable = false)
    private int status;
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;

    protected ItemRequestEntity() {

    }

    public ItemRequestEntity(String email, String keyword, String categories) {
        this.email = email;
        this.keyword = keyword;
        this.categories = categories;
        this.status = 0;
        this.createDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemRequestEntity that = (ItemRequestEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
