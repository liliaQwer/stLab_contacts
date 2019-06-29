package email;

import org.stringtemplate.v4.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class EmailTemplate {

    public static String getTemplates() throws MalformedURLException {
        //ST hello = new ST("Hello, <name>");
        //hello.add("name", "World");
        //System.out.println(hello.render());
        System.out.println("Before template");
        URL wrong =EmailTemplate.class.getClassLoader().getResource("templates");
        System.out.println(wrong);
        //URL wrong = Thread.currentThread().getContextClassLoader().getResource("templates");
        URL correct = new URL(wrong.toString().replaceAll("/$", ""));
        System.out.println(correct);
        STRawGroupDir stGroupDir = new STRawGroupDir(correct, null, '$', '$');
        //STGroup stGroupDir = new STGroupDir("templates",'$','$');
        System.out.println(stGroupDir.getRootDirURL());
        //STGroupDir.verbose = true;
        ST st = stGroupDir.getInstanceOf("test");
        System.out.println("ST:" + st);
        st.add("param", "Лиля");
        System.out.println("template: " + st.render());
        return "**********";
    }

    public static void sendEmail() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("stlab.kar","qazxsW123");
                    }
                });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("liliaqwer@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("andrew.qwer87@gmail.com"));
        message.setSubject("test email");
        message.setText("Hello, from me!");
        Transport.send(message);
    }
}
