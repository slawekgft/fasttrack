package com.gft.ft.allegro

import com.gft.ft.Startpoint
import com.icegreen.greenmail.util.GreenMail
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.fest.assertions.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by e-srwn on 2016-09-16.
 */
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Startpoint.class])
@WebAppConfiguration
@IntegrationTest
class MainIntTest extends Specification {

//    @Shared def httpClient

    @Shared def mailServer

    @Autowired TestRestTemplate template

    def setupSpec() {
        mailServer = new GreenMail()
        mailServer.start()
    }

    def cleanupSpec() {
        mailServer.stop()
    }

    def "Register new item"() {

        when:
        def object = template.getForObject("http://localhost:8080/find?cat=komputery&name=abcdefgh&email=slawomir.wegrzyn@gft.com", String.class)
        println(object)

        then:
        Assertions.assertThat(object.toString()).contains("ok")
    }

}
