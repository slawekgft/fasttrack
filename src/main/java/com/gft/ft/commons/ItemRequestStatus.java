package com.gft.ft.commons;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by e-srwn on 2016-09-09.
 */
public enum ItemRequestStatus {
    NEW, IN_PROGRESS, ERROR, FINISHED;

    public static final Optional<ItemRequestStatus> valueOf(final int ordinal) {
        return Arrays.stream(values()).filter((p) -> p.ordinal()==ordinal).collect(Collectors.reducing((a, b) -> null));
    }
}
