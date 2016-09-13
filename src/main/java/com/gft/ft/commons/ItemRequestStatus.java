package com.gft.ft.commons;

/**
 * Created by e-srwn on 2016-09-09.
 */
public enum ItemRequestStatus {
    NEW, IN_PROGRESS, ERROR, FINISHED;

    public static final ItemRequestStatus valueOf(int ordinal) {
        for(ItemRequestStatus itemRequestStatus : values()) {
            if(itemRequestStatus.ordinal() == ordinal) {
                return itemRequestStatus;
            }
        }

        return null;
    }
}
