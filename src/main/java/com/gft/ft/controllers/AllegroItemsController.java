package com.gft.ft.controllers;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.DBOperationProblemException;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.TooMuchItemsFoundException;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.requests.RequestsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import static com.gft.ft.commons.PresentationUtils.*;
import static com.gft.ft.commons.PresentationUtils.list;

/**
 * Created by e-srwn on 2016-09-06.
 */
@RestController
public class AllegroItemsController {

    public static final String WEB_ITEMS_REQUEST_PROBLEM_MSG = "web.items.registration_problem";
    public static final String WEB_ITEMS_REQUEST_REGISTERED_MSG = "web.items.request_registered";
    public static final String WEB_ITEMS_NO_REQUESTS_MSG = "web.items.no_requests";
    public static final String WEB_ITEMS_TOO_MANY_ITEMS_MSG = "web.items.too_many_items";
    public static final String WEB_ITEMS_LABEL_ITEM_MSG = "web.items.label.item";

    private static Logger log = LoggerFactory.getLogger(AllegroItemsController.class);

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AllegroService allegroService;

    @RequestMapping(value = "/find", produces = MediaType.TEXT_HTML_VALUE)
    public String requestItem(@Size(min = 3) @RequestParam(value = "cat") String categoryNameFilter,
                              @Size(min = 3) @RequestParam(value = "name") String keyword,
                              @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$") @RequestParam(value = "email") String email) {

        log.debug("requestItem for " + email);
        String info = getMessage(WEB_ITEMS_REQUEST_REGISTERED_MSG);
        final Collection<Integer> categoriesIds = allegroService.findCategoriesIds(categoryNameFilter);
        final ItemRequest itemRequest = new ItemRequest(email, keyword, categoriesIds);
        try {
            requestsService.registerRequest(itemRequest);
        } catch (DBOperationProblemException e) {
            info = getMessage(WEB_ITEMS_REQUEST_PROBLEM_MSG);
        } catch (TooMuchItemsFoundException e) {
            info = prepareListInfo(getMessage(WEB_ITEMS_TOO_MANY_ITEMS_MSG), e.getItems());
        }

        return paragraph(info);
    }

    private String prepareListInfo(String message, Collection<Item> items) {
        final StringBuffer sb = new StringBuffer(message);
        if(CollectionUtils.size(items) > 0) {
            StringBuffer listOfItems = new StringBuffer();
            for(Item it : items) {
                listElem(listOfItems, getMessage(WEB_ITEMS_LABEL_ITEM_MSG) + " " + it.getId());
            }
            sb.append(list(listOfItems.toString()));
        }
        return sb.toString();
    }

    @RequestMapping(value = "/check", produces = MediaType.TEXT_HTML_VALUE)
    public String checkRequests(@Valid @RequestParam(value = "email") String email) {
        String response = getMessage(WEB_ITEMS_NO_REQUESTS_MSG);
        final Collection<ItemRequest> requests = requestsService.getRequests(email);
        if(CollectionUtils.isNotEmpty(requests)) {
            response = paragraph(list(itemReqestsList(requests)));
        }

        return response;
    }

    private String getMessage(String msg) {
        return messageSource.getMessage(msg, null, Locale.US);
    }

    private String itemReqestsList(Collection<ItemRequest> requests) {
        StringBuffer listItems = new StringBuffer();
        for(ItemRequest ir : requests) {
            Collection<String> categories = allegroService.getCategoriesNames(new HashSet<>(ir.getCategories()));
            final String replacement = "'" + ir.getKeyword() + "', {" + StringUtils.join(categories, "|") + "}, " + ir.getStatus();
            listElem(listItems, replacement);
        }
        return listItems.toString();
    }

}
