package com.gft.ft.commons;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by e-srwn on 2016-09-15.
 */
public class PresentationUtils {
    private PresentationUtils() {

    }

    public static String paragraph(String message) {
        return StringUtils.replace("<p>{0}</p>", "{0}", message);
    }

    public static String list(String listElements) {
        return StringUtils.replace("<ol>{0}</ol>", "{0}", listElements);
    }

    public static void listElem(StringBuffer out, String listItem) {
        out.append(StringUtils.replace("<li>{0}</li>", "{0}", listItem));
    }

}
