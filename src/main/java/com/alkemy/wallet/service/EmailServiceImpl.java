package com.alkemy.wallet.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService{

    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    public void sendVerificationEmail(String toEmail, String verificationLink) {
        System.out.println("ENVIANDO MAIL A " + toEmail);
        Email from = new Email("ezequielleandro.el@gmail.com");
        Email to = new Email(toEmail);
        String subject = "Verificá tu cuenta";
        Content content = new Content("text/html",
                "<p>Bienvenido a AlkyWallet</p>" +
                        "<p>Haz clic en el siguiente enlace para verificar tu cuenta:</p>" +
                        "<a href=\"" + verificationLink + "\">Verificar cuenta</a>");

        Mail mail = new Mail(from, subject, to, content);

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
