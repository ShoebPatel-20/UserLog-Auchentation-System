package com.demo.userlog;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
//import javax.mail.*;
//import javax.mail.internet.*;

public class EmailSender {
    public static void sendOtp(String recipientEmail, String otp) {
        final String fromEmail = "example.gmail.com"; // your email
        final String emailPassword = "xyz"; // use App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            msg.setSubject("Your OTP Code");
            msg.setText("Your OTP code is: " + otp);
            Transport.send(msg);
            System.out.println("OTP Sent Successfully " + otp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
