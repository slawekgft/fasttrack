package com.gft.ft.allegro;

import com.gft.ft.allegrointerface.DoGetCatsDataRequest;
import com.gft.ft.allegrointerface.DoGetItemsListRequest;
import com.gft.ft.allegrointerface.ObjectFactory;

/**
 * Created by e-srwn on 2016-09-13.
 */
public class AllegroObjectFactory extends ObjectFactory {
    private String apiKey;
    private Integer countryId;

    public AllegroObjectFactory(String apiKey, Integer countryId) {
        this.apiKey = apiKey;
        this.countryId = countryId;
    }

    public DoGetCatsDataRequest createDoGetCatsDataRequest() {
        final DoGetCatsDataRequest doGetCatsDataRequest = super.createDoGetCatsDataRequest();
        doGetCatsDataRequest.setWebapiKey(apiKey);
        doGetCatsDataRequest.setCountryId(countryId);

        return doGetCatsDataRequest;
    }

    public DoGetItemsListRequest createDoGetItemsListRequest() {
        final DoGetItemsListRequest doGetItemsListRequest = super.createDoGetItemsListRequest();
        doGetItemsListRequest.setWebapiKey(apiKey);
        doGetItemsListRequest.setCountryId(countryId);

        return doGetItemsListRequest;
    }
}
