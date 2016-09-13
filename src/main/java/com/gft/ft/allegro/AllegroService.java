package com.gft.ft.allegro;

import com.gft.ft.allegrointerface.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.upperCase;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Component
public class AllegroService {

    private static final Logger log = LoggerFactory.getLogger(AllegroService.class);

    private @Value("${allegro.ws.webapi_key}") String apiKey;
    private @Value("${allegro.ws.country_id}") Integer countryId;

    private ServiceService allegroService;
    private ObjectFactory objectFactory;

    @PostConstruct
    public void init() {
        allegroService = new ServiceService();
        objectFactory = new AllegroObjectFactory(apiKey, countryId);
    }

    public Collection<Integer> findCategoriesIds(String categoryNameFilter) {
        final List<CatInfoType> catInfoTypes = getCatsInfo();

        return catInfoTypes.stream().filter(filterByName(categoryNameFilter)).map(categoryId()).collect(Collectors.toList());
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

    public Collection<String> getCategoriesNames(Set<Integer> categoriesIds) {
        log.debug("getCategoriesNames");

        final List<CatInfoType> catInfoTypes = getCatsInfo();

        return catInfoTypes.stream().filter(filterByIds(categoriesIds)).map(categoryName()).collect(Collectors.toList());
    }

    private List<CatInfoType> getCatsInfo() {
        final ServicePort servicePort = allegroService.getServicePort();
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
}
