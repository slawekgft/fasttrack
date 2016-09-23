package com.gft.ft.daos.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by e-srwn on 2016-09-23.
 */
@Converter(autoApply = true)
public class InstantPersistenceConverter implements AttributeConverter<Instant,Timestamp> {
    @Override
    public java.sql.Timestamp convertToDatabaseColumn(Instant entityValue) {
        return java.sql.Timestamp.from(entityValue);
    }

    @Override
    public Instant convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        return databaseValue.toInstant();
    }
}