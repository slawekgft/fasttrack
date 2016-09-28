package com.gft.ft.messages;

import com.gft.ft.allegro.AllegroService;
import com.gft.ft.commons.allegro.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gft.ft.commons.PresentationUtils.*;

/**
 * Created by e-srwn on 2016-09-07.
 */
@Service
public class MessagesService {

    public static final String WEB_ITEMS_MAIL_TEXT_MSG = "web.items.mail.text";
    public static final String WEB_ITEMS_MAIL_SUBJ_MSG = "web.items.mail.subj";
    public static final String AUCTION_URL_ALLEGRO = "http://allegro.pl";

    @Value("${email.from}")
    private String mailFrom;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AllegroService allegroService;

    private class MailModel {
        private String url;
        private String itemName;

        public MailModel(String url, String itemName) {
            this.url = url;
            this.itemName = itemName;
        }

        public String getUrl() {
            return url;
        }

        public String getItemName() {
            return itemName;
        }
    }

    public void mailItemAvailable(String email, Set<Item> userItems) {
        sendInformationEmail(email, userItems.stream().map(map2MailModel()).collect(Collectors.toSet()));
    }

    private Function<? super Item, ? extends MailModel> map2MailModel() {
        return (Function<Item, MailModel>) item -> {
            String url = AUCTION_URL_ALLEGRO;
            MailModel mailModel = new MailModel(url, item.getName());
            return mailModel;
        };
    }

    private void sendInformationEmail(final String email, final Set<MailModel> model) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(email);
            message.setSubject(getMessage(WEB_ITEMS_MAIL_SUBJ_MSG));
            message.setFrom(mailFrom);
            message.setText(html(provideBody(model)), true);
        };
        this.mailSender.send(preparator);
    }

    private String html(String body) {
        return "<html><body>" + body + "</body></html>";
    }

    private String provideBody(Set<MailModel> model) {
        StringBuffer sb = new StringBuffer();
        for (MailModel mm : model) {
            listElem(sb, getMessage(WEB_ITEMS_MAIL_TEXT_MSG)
                    .replaceFirst("\\{0\\}", mm.getUrl())
                    .replaceFirst("\\{1\\}", mm.getItemName()));
        }

        return paragraph(list(sb.toString()));
    }

    private String getMessage(String msg) {
        return messageSource.getMessage(msg, null, Locale.US);
    }
}
