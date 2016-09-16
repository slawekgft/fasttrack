package com.gft.ft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by e-srwn on 2016-09-07.
 */
@SpringBootApplication
@ContextConfiguration("/appcontext.xml")
@EnableJpaRepositories("com.gft.ft.daos.repos")
@EnableScheduling
public class Startpoint {
    private static Logger log = LoggerFactory.getLogger(Startpoint.class);

    public static void main(String[] args) {
        log.info("welcome!");
        SpringApplication.run(Startpoint.class, args);
        log.info("goodbye!");
    }

    @Bean
    public TestRestTemplate restTemplate() {
        return new TestRestTemplate();
    }
}
