package com.gft.ft.messages;

import com.gft.ft.allegro.AllegroOperationsService;
import com.gft.ft.commons.PresentationUtils;
import com.gft.ft.commons.allegro.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gft.ft.commons.PresentationUtils.list;
import static com.gft.ft.commons.PresentationUtils.paragraph;

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
    private AllegroOperationsService allegroOperationsService;

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
        sendInformationEmail(email, userItems.stream()
                .map(item -> new MailModel(AUCTION_URL_ALLEGRO, item.getName())
                ).collect(Collectors.toSet()));
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
        model.stream()
                .map(mailModel -> getMessage(WEB_ITEMS_MAIL_TEXT_MSG)
                        .replaceFirst("\\{0\\}", mailModel.getUrl())
                        .replaceFirst("\\{1\\}", mailModel.getItemName()))
                .map(PresentationUtils::wrapInBulletTag)
                .forEach(sb::append);

        return paragraph(list(sb.toString()));
    }

    private String getMessage(String msg) {
        return messageSource.getMessage(msg, null, Locale.US);
    }
}
