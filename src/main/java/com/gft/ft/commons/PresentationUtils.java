package com.gft.ft.commons;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by e-srwn on 2016-09-15.
 */
public final class PresentationUtils {
    private PresentationUtils() {

    }

    public final static String paragraph(final String message) {
        return StringUtils.replace("<p>{0}</p>", "{0}", message);
    }

    public final static String list(final String listElements) {
        return StringUtils.replace("<ol>{0}</ol>", "{0}", listElements);
    }

    public final static String wrapInBulletTag(final String listItem) {
        return StringUtils.replace("<li>{0}</li>", "{0}", listItem);
    }

}
