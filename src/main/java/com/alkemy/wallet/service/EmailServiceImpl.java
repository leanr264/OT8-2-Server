package com.alkemy.wallet.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService{

    @Value("${email.from}")
    private String fromEmail;
    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;
    @Value("${sendgrid.template.verify}")
    private String verifyTemplateId;


    @Override
    public void sendVerificationEmail(String toEmail, String verificationLink) {
        System.out.println("ENVIANDO MAIL A " + toEmail);
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(verifyTemplateId);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("verification_link", verificationLink);
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("SendGrid status: " + response.getStatusCode());
            System.out.println("SendGrid body: " + response.getBody());
            System.out.println("SendGrid headers: " + response.getHeaders());

        } catch (Exception e) {
            throw new RuntimeException("Error enviando verificación: " + e.getMessage());
        }
    }
}
