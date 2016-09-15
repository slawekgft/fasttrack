package com.gft.ft.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by e-srwn on 2016-09-15.
 */
public class ItemsServiceMock {
    private static Logger log = LoggerFactory.getLogger(ItemsServiceMock.class);

    @Scheduled(cron = "*/5 * * * * MON-FRI")
    public void periodicalCheck() {
        log.debug("mock periodical check");
    }
}
