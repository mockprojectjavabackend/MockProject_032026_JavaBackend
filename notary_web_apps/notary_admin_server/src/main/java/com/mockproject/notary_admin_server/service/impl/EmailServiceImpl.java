package com.mockproject.notary_admin_server.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.EmailErrorCode;
import com.mockproject.notary_admin_server.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMAIL-SERVICE")
public class EmailServiceImpl implements EmailService {

    private static final String INVITE_TEMPLATE_PATH = "templates/email/invite.html";
    private static final int INVITE_EXPIRY_HOURS = 24;

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Override
    public void sendInvitationEmail(String toEmail, String fullName, String inviteLink) {
        log.info("Sending invitation email to: {}", toEmail);

        String html = loadTemplate(INVITE_TEMPLATE_PATH)
                .replace("{{fullName}}", escapeHtml(fullName))
                .replace("{{inviteLink}}", inviteLink)
                .replace("{{expiryHours}}", String.valueOf(INVITE_EXPIRY_HOURS));

        sendHtmlEmail(
                toEmail,
                "Kích hoạt tài khoản Công chứng viên",
                html);

        log.info("Invitation email sent successfully to: {}", toEmail);
    }

    private String loadTemplate(String classpathPath) {
        try {
            ClassPathResource resource = new ClassPathResource(classpathPath);
            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Failed to load email template: {}", classpathPath, e);
            throw new AppException(EmailErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // multipart = true → supports HTML + future inline images
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(message);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage(), e);
            throw new AppException(EmailErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
