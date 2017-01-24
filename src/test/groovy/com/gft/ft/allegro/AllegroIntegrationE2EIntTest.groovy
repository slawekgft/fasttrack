package com.gft.ft.allegro

import com.gft.ft.AllegroItemsApplication
import com.icegreen.greenmail.util.GreenMail
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import static org.fest.assertions.Assertions.assertThat

/**
 * Created by e-srwn on 2016-09-16.
 */
/**
 * Ten test może działać wyłącznie z produkcyjnym Allegro
 * ze względu na sprawdzenie produkcyjnych nazw kategorii i towarów
 *
 * mvn -P prd -Dtest=AllegroIntegrationE2EIntTest test
 */
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [AllegroItemsApplication.class])
@WebAppConfiguration
@IntegrationTest
@TestPropertySource(["/application.properties", "/application-prd.properties"])
class AllegroIntegrationE2EIntTest extends Specification {

    public static final String FIND_URL = "http://localhost:8080/find"
    public static final String LIST_URL = "http://localhost:8080/list"
    public static final String REGISTERED_MSG = "Request registered."
    public static final String TOO_MANY_ITEMS_MSG = "Many items are available now"
    public static final String REGISTERED_ITEMS_LIST_MSG = "Registered requests"
    public static final String NO_REQUESTS_FOUND_MSG = "No requests found"

    @Shared def GreenMail mailServer

    TestRestTemplate template = new TestRestTemplate()

    def setupSpec() {
        mailServer = new GreenMail()
        mailServer.start()
    }

    def cleanupSpec() {
        mailServer.stop()
    }

    def "Register new item"() {
        when:
        def object = template.postForObject(
                FIND_URL + "?cat=komputery&name=abcdefgh&email=user@gft.com",
                null,
                String.class)

        then:
        assertThat(object.toString()).contains(REGISTERED_MSG)
    }

    def "Multiple items registration" () {
        setup:
        def url = FIND_URL + "?cat=" + category + "&name=" + name + "&email=" + user

        expect:
        def String response = template.postForObject(url, null, String.class)
        println(">>>> " + url)
        println(">>>> " + response)
        registered == response.contains(REGISTERED_MSG)
        tooMany == response.contains(TOO_MANY_ITEMS_MSG)

        where:
        category            |   name            |   user            | registered | tooMany
        "komputery"         | "atari"           | "user"            | false      | false
        "książki"           | "Br"              | "user@dom.com"    | false      | false
        "ks"                | "Brown"           | "user@dom.com"    | false      | false
        "komiks"            | "t-34 t-55"       | "user@dom.com"    | true       | false
        "komputery"         | "abcxyzust"       | "user@dom.com"    | true       | false
        "komputery"         | "atari"           | "user@.com"       | false      | true
        "komputery"         | "laptop"          | "user@dom.com"    | false      | true
        "książki"           | "Brown"           | "user@dom.com"    | false      | true
    }

    def "Show registered" () {
        given:
        def userEmail = "user@gft.com"
        template.postForObject(FIND_URL + "?cat=komputery&name=abcdefgh&email=" + userEmail, null, String.class)

        when:
        def responseFound = template.getForObject(LIST_URL + "?email=" + userEmail, String.class)
        def responseNotFound = template.getForObject(LIST_URL + "?email=notfound@user.com", String.class)

        then:
        assertThat(responseFound).contains(REGISTERED_ITEMS_LIST_MSG)
        assertThat(responseNotFound).contains(NO_REQUESTS_FOUND_MSG)
    }
}
