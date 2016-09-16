package com.gft.ft.allegro;

import com.gft.ft.allegrointerface.*;
import com.gft.ft.commons.ItemRequest;
import com.gft.ft.commons.allegro.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.upperCase;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class AllegroService {

    private static final Logger log = LoggerFactory.getLogger(AllegroService.class);
    public static final String CATEGORY_FILTER_NAME = "category";
    public static final String SEARCH_FILTER_NAME = "search";

    private @Value("${allegro.ws.webapi_key}") String apiKey;
    private @Value("${allegro.ws.country_id}") Integer countryId;

    private ServiceService allegroWS;
    private ObjectFactory objectFactory;

    @PostConstruct
    public void init() {
        allegroWS = new ServiceService();
        objectFactory = new AllegroObjectFactory(apiKey, countryId);
    }

    public Collection<Integer> findCategoriesIds(String categoryNameFilter) {
        final List<CatInfoType> catInfoTypes = getCatsInfo();

        return catInfoTypes.stream().filter(filterByName(categoryNameFilter)).map(categoryId()).collect(Collectors.toList());
    }

    public Collection<String> getCategoriesNames(Set<Integer> categoriesIds) {
        log.debug("getCategoriesNames");

        final List<CatInfoType> catInfoTypes = getCatsInfo();

        return catInfoTypes.stream().filter(filterByIds(categoriesIds)).map(categoryName()).collect(Collectors.toList());
    }

    public Set<Item> findItemsForCategoryAndKeyword(ItemRequest itemRequest) {
        Set<Item> foundItems = new HashSet<>();
        final ServicePort servicePort = allegroWS.getServicePort();
        final DoGetItemsListRequest doGetItemsListRequest = objectFactory.createDoGetItemsListRequest();
        for(Integer categoryId : itemRequest.getCategories()) {
            final ArrayOfFilteroptionstype arrayOfFilteroptionstype = objectFactory.createArrayOfFilteroptionstype();
            arrayOfFilteroptionstype.getItem().addAll(filterItems()
                    .addSearch(CATEGORY_FILTER_NAME, Integer.toString(categoryId))
                    .addSearch(SEARCH_FILTER_NAME, itemRequest.getKeyword())
                    .build());
            doGetItemsListRequest.setFilterOptions(arrayOfFilteroptionstype);
            final DoGetItemsListResponse doGetItemsListResponse = servicePort.doGetItemsList(doGetItemsListRequest);

            if (doGetItemsListResponse.getItemsCount() > 0) {
                foundItems.addAll(doGetItemsListResponse.getItemsList().getItem().stream().map(map2Item()).collect(Collectors.toSet()));
            }
        }

        return foundItems;
    }

    private Function<? super ItemsListType, ? extends Item> map2Item() {
        return new Function<ItemsListType, Item>() {
            @Override
            public Item apply(ItemsListType itemsListType) {
                Item item = new Item();
                item.setId(itemsListType.getItemId());
                item.setName(itemsListType.getItemTitle());

                return item;
            }
        };
    }

    private FilterOptionsBuilder filterItems() {
        return new FilterOptionsBuilder();
    }

    private Function<? super CatInfoType, ? extends Integer> categoryId() {
        return new Function<CatInfoType, Integer>() {
            @Override
            public Integer apply(CatInfoType catInfoType) {
                return catInfoType.getCatId();
            }
        };
    }

    private Predicate<? super CatInfoType> filterByName(final String categoryNameFilter) {
        return new Predicate<CatInfoType>() {
            @Override
            public boolean test(final CatInfoType catInfoType) {
                return upperCase(catInfoType.getCatName()).contains(upperCase(categoryNameFilter));
            }
        };
    }

    private List<CatInfoType> getCatsInfo() {
        final ServicePort servicePort = allegroWS.getServicePort();
        final DoGetCatsDataRequest doGetCatsDataRequest = objectFactory.createDoGetCatsDataRequest();
        final DoGetCatsDataResponse doGetCatsDataResponse = servicePort.doGetCatsData(doGetCatsDataRequest);
        return doGetCatsDataResponse.getCatsList().getItem();
    }

    private Predicate<CatInfoType> filterByIds(final Set<Integer> categories) {
        return new Predicate<CatInfoType>() {
            @Override
            public boolean test(CatInfoType catInfoType) {
                return categories.contains(catInfoType.getCatId());
            }
        };
    }

    static Function<CatInfoType, String> categoryName() {
        return new Function<CatInfoType, String>() {
            @Override
            public String apply(CatInfoType catInfoType) {
                return catInfoType.getCatName();
            }
        };
    }

    private class FilterOptionsBuilder {
        private List<FilterOptionsType> filters;

        public FilterOptionsBuilder() {
            this.filters = new ArrayList<>();
        }

        public FilterOptionsBuilder addSearch(String id, String ... value) {
            FilterOptionsType filter = objectFactory.createFilterOptionsType();
            filter.setFilterId(id);
            ArrayOfString filterValues = objectFactory.createArrayOfString();
            for (String v : value) {
                filterValues.getItem().add(v);
            }
            filter.setFilterValueId(filterValues);
            filters.add(filter);

            return this;
        }

        public List<FilterOptionsType> build() {
            return filters;
        }
    }
}
