package com.gft.ft;

import com.gft.ft.controllers.AllegroItemsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by e-srwn on 2016-09-07.
 */
@SpringBootApplication
@EnableJpaRepositories("com.gft.ft.daos.repos")
@EnableScheduling
public class Startpoint {
    private static Logger log = LoggerFactory.getLogger(Startpoint.class);

    public static void main(String[] args) {
        log.info("welcome!");
        SpringApplication.run(Startpoint.class, args);
    }
}
