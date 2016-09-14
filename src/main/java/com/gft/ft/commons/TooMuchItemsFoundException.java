package com.gft.ft.commons;

import com.gft.ft.commons.allegro.Item;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by e-srwn on 2016-09-07.
 */
public class TooMuchItemsFoundException extends Exception {
    private Collection<Item> items = new ArrayList<>();
    public TooMuchItemsFoundException(Collection<Item> items) {
        items.addAll(items);
    }
}
