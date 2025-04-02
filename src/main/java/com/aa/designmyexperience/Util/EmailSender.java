package com.aa.designmyexperience.Util;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendEmailImages(String toEmail, String subject, String body, Map<String, String> mapImages) {

        final String fromEmail = "designmyexperience@gmail.com";
        final String password = "zpub jiuc osrs dgpv";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            //message.setText(body);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // adds inline image attachments
            if (mapImages != null && mapImages.size() > 0) {
                Set<String> setImageID = mapImages.keySet();

                for (String contentId : setImageID) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setHeader("Content-ID", "<" + contentId + ">");
                    imagePart.setDisposition(MimeBodyPart.INLINE);

                    String imageFilePath = mapImages.get(contentId);
                    try {
                        imagePart.attachFile(imageFilePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    multipart.addBodyPart(imagePart);
                }
            }

            message.setContent(multipart);


            //message.setText(body);


            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "designmyexperience@gmail.com";
        final String password = "zpub jiuc osrs dgpv";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);


            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}