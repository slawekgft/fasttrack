package com.gft.ft.messages;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by e-srwn on 2016-09-15.
 */
@Configuration
public class MailConfig {

    private Logger log = LoggerFactory.getLogger(MailConfig.class);

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private Integer port;

    @Value("${email.user}")
    private String username;

    @Value("${email.pass}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {

        log.debug("create javaMailService");

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        if(StringUtils.isNotBlank(username)) {
            javaMailSender.setUsername(username);
            javaMailSender.setPassword(password);
        }

        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        if(StringUtils.isNotBlank(username)) {
            properties.setProperty("mail.smtp.auth", "true");
        }
        properties.setProperty("mail.smtp.starttls.enable", "false");
        properties.setProperty("mail.debug", "true");

        return properties;
    }
}