package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MailServiceCustom;
import com.mycompany.myapp.service.dto.SysUserDTO;
import java.util.Locale;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MailServiceCustomImpl implements MailServiceCustom {

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;
    private final String EMAIL_FROM = "tuannguyen190499@gmail.com";

    public MailServiceCustomImpl(MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendCodeForgetPwd(SysUserDTO sysUserDTO, String otpCode) {
        try {
            Context context = new Context();
            context.setVariable("fullName", sysUserDTO.getFullName());
            context.setVariable("otpCode", otpCode);
            sendEmailFromTemplate(sysUserDTO, context, "mail/passwordForgetEmail", "email.forgetPassword.title");
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @Async
    public void sendEmailFromTemplate(SysUserDTO sysUserDTO, Context context, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("vi");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        //        onSendEmail("traffic_id_v2@viettel.com.vn", subject, content, false, true);
        this.onSendEmail(sysUserDTO.getEmail(), subject, content, false, true);
    }

    public boolean onSendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        Thread thread = new Thread(() -> sendEmail(to, subject, content, isMultipart, isHtml));
        thread.start();
        return true;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        //        EmailConfigDTO emailConfigDTO = emailConfigService.getConfig();
        //
        JavaMailSender mailSender = createJavaMailSender();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(EMAIL_FROM);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private JavaMailSender createJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(EMAIL_FROM);
        mailSender.setPassword("tlfo enqc uomv wdil");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        //        props.put("mail.smtp.ssl.trust", "*");
        //        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return mailSender;
    }
}
