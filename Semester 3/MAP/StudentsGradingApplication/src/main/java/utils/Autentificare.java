package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

 class MailSender implements Runnable{
    private String content,header,email;
    public MailSender(String email,String header,String content){
        this.email=email;
        this.header=header;
        this.content=content;
    }
    @Override
    public void run() {
        final String username = "petrut.damian99@gmail.com";
        final String password = Autentificare.getPass();

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("petrut.damian99@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(header);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
public class Autentificare {
    public static void sendMail(String email,String header,String content){
        Thread t = new Thread(new MailSender(email,header,content));
        t.start();
    }



















































    private static String pass="mutalisksharpshooter22@a";
    public static String getPass(){
        return pass;
    }
}
