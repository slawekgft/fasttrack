package com.gft.ft.context;

import com.gft.ft.allegro.AllegroObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by e-srwn on 2016-09-22.
 */
@Configuration
public class BeanConfig {

    private @Value("${allegro.ws.webapi_key}") String apiKey;
    private @Value("${allegro.ws.country_id}") Integer countryId;

    @Bean
    public AllegroObjectFactory allegroObjectFactory() {
        return new AllegroObjectFactory(apiKey, countryId);
    }
}
