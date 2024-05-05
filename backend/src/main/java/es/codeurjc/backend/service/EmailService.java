package es.codeurjc.backend.service;

import jakarta.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    final private String BASE_URL = "https://10.100.139.188:8443";

    @Autowired
    private MustacheResourceTemplateLoader mustacheTemplateLoader;

    public void sendActivation(String to, String username) {
        Map<String, Object> context = new HashMap<>();
        context.put("activationUrl", BASE_URL + "/user/activation/" + username);
        sendHtmlMessage(to, "Activation account", "email_template", context);
    }

    public void sendHtmlMessage(String to, String subject, String templateName, Map<String, Object> context) {
        try {
            Template template = Mustache.compiler().compile(mustacheTemplateLoader.getTemplate(templateName));
            StringWriter writer = new StringWriter();
            template.execute(context, writer);
            String htmlContent = writer.toString();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}