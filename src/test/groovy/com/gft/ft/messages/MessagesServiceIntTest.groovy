package com.gft.ft.messages

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.ServerSetupTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification

import static com.gft.ft.tests.TestUtil.createItemsSet

/**
 * Created by e-srwn on 2016-09-15.
 */
@ContextConfiguration(locations = "classpath:app-config-empty.xml")
@TestPropertySource(["/application.properties", "/application-dev.properties"])
class MessagesServiceIntTest extends Specification {

    public static final String ITEMS_AVAIL_SUBJECT_MSG = "Allegro items available"
    @Autowired
    MessagesService messagesService

    @Shared
    GreenMail server;

    def setupSpec() {
        server = new GreenMail(ServerSetupTest.SMTP);
        server.start()
        println(">>>>>")
    }

    def cleanupSpec() {
        server.stop();
    }

    def "MailItemAvailable"() {
        expect:
        messagesService.mailItemAvailable(email, items)
        def receivedMessages = server.getReceivedMessages()
        receivedMessages.size() == msgCount
        receivedMessages[0].getHeader("Subject")[0].equals(subject)

        where:
        email                      | items              | msgCount      | subject
        "slawomir.wegrzyn@gft.com" | createItemsSet(10) | 1             | ITEMS_AVAIL_SUBJECT_MSG
    }
}
