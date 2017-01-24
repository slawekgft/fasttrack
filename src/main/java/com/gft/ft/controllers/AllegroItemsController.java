package com.gft.ft.controllers;

import com.gft.ft.allegro.AllegroOperationsService;
import com.gft.ft.commons.DBOperationProblemException;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.PresentationUtils;
import com.gft.ft.commons.TooMuchItemsFoundException;
import com.gft.ft.commons.allegro.Item;
import com.gft.ft.requests.RequestsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.function.Function;

import static com.gft.ft.commons.PresentationUtils.list;
import static com.gft.ft.commons.PresentationUtils.paragraph;

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
    public static final String WEB_VALIDATION_LENGTH_MSG = "web.validation.msg.length";
    public static final String WEB_VALIDATION_EMAIL_MSG = "web.validation.msg.email";
    public static final String WEB_ITEMS_REQUESTS_REGISTERED_MSG = "web.items.requests_registered";

    private static Logger log = LoggerFactory.getLogger(AllegroItemsController.class);

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AllegroOperationsService allegroOperationsService;

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String requestItem(@Length(min = 3, max = 50, message = "Proszę podać poprawną nazwę katalogu!") @RequestParam(value = "cat") String categoryNameFilter,
                              @Length(min = 3, max = 50, message = "Proszę podać poprawną nazwę przedmiotu!") @RequestParam(value = "name") String keyword,
                              @Email @RequestParam(value = "email") String email) {

        log.debug("requestItem for " + email);
        String info = getMessage(WEB_ITEMS_REQUEST_REGISTERED_MSG);
        try {
            final Collection<Integer> categoriesIds = allegroOperationsService.findCategoriesIds(categoryNameFilter);
            final ItemRequest itemRequest = new ItemRequest(email, keyword, categoriesIds);
            requestsService.registerRequest(itemRequest);
        } catch (DBOperationProblemException e) {
            info = getMessage(WEB_ITEMS_REQUEST_PROBLEM_MSG);
        } catch (TooMuchItemsFoundException e) {
            info = prepareListInfo(getMessage(WEB_ITEMS_TOO_MANY_ITEMS_MSG), e.getItems());
        }

        return paragraph(info);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String listRequests(@RequestParam(value = "email") String email) {
        String response = paragraph(getMessage(WEB_ITEMS_NO_REQUESTS_MSG));
        final Collection<ItemRequest> requests = requestsService.getRequests(email);
        if (CollectionUtils.isNotEmpty(requests)) {
            response = paragraph(getMessage(WEB_ITEMS_REQUESTS_REGISTERED_MSG))
                    + paragraph(list(itemReqestsList(requests)));
        }

        return response;
    }

    private String getMessage(String msg) {
        return messageSource.getMessage(msg, null, Locale.US);
    }

    private String itemReqestsList(Collection<ItemRequest> requests) {
        StringBuffer listItems = new StringBuffer();
        requests.stream()
                .map(commaSeparatedList())
                .map(PresentationUtils::wrapInBulletTag)
                .forEach(listItems::append);
        return listItems.toString();
    }

    private Function<ItemRequest, String> commaSeparatedList() {
        return ir ->
        {
            final Collection<String> categories = allegroOperationsService.getCategoriesNames(new HashSet<>(ir.getCategories()));
            return "'" + ir.getKeyword() + "', {" + StringUtils.join(categories, "|") + "}, " + ir.getStatus();
        };
    }

    private String prepareListInfo(String message, Collection<Item> items) {
        final StringBuffer sb = new StringBuffer(message);
        if (CollectionUtils.size(items) > 0) {
            StringBuffer listOfItems = new StringBuffer();
            items.stream()
                .map(mailListElements())
                .map(PresentationUtils::wrapInBulletTag)
                .forEach(listOfItems::append);
            sb.append(list(listOfItems.toString()));
        }
        return sb.toString();
    }

    private Function<? super Item, ? extends String> mailListElements() {
        return item -> getMessage(WEB_ITEMS_LABEL_ITEM_MSG) + " " + item.getId();
    }
}
