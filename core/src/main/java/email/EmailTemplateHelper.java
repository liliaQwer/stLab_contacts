package email;

import org.stringtemplate.v4.*;
import view.ContactEmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Stream;

public class EmailTemplateHelper {
    private static Properties smtpProps;
    static{
        try (InputStream input = EmailTemplateHelper.class.getClassLoader().getResourceAsStream("mail.properties")) {
            smtpProps = new Properties();
            smtpProps.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<TemplateType> getAllTemplates() throws MalformedURLException {
        URL urlSlash = EmailTemplateHelper.class.getClassLoader().getResource("templates");
        //URL wrong = Thread.currentThread().getContextClassLoader().getResource("templates");
        URL templateUrl = new URL(urlSlash.toString().replaceAll("/$", ""));
        STRawGroupDir stGroupDir = new STRawGroupDir(templateUrl, null, '$', '$');
        ArrayList<TemplateType> templateList = new ArrayList<>();
        FileSystem fileSystem = null;
        try {
            URI uri = templateUrl.toURI();
            Path myPath;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                myPath = fileSystem.getPath("/templates");
            } else {
                myPath = Paths.get(uri);
            }
            Stream<Path> walk = Files.walk(myPath, 1);
            for (Iterator<Path> it = walk.iterator(); it.hasNext();){
                String fileName = it.next().toString();
                if (fileName.endsWith(".st")){
                    String templateName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.indexOf(".st"));
                    TemplateType templateType = new TemplateType();
                    templateType.setName(templateName);
                    ST st = stGroupDir.getInstanceOf(templateName);
                    st.add("name", "<name>");
                    templateType.setTemplate(st.render());
                    templateList.add(templateType);
                }
            }
            fileSystem.close();
        } catch (Exception e) {
            if (fileSystem!= null) {
                try {
                    fileSystem.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return templateList;
    }

    public static String getTemplate(String templateName, String name) throws MalformedURLException {
        URL urlSlash = EmailTemplateHelper.class.getClassLoader().getResource("templates");
        //URL wrong = Thread.currentThread().getContextClassLoader().getResource("templates");
        URL templateUrl = new URL(urlSlash.toString().replaceAll("/$", ""));
        STRawGroupDir stGroupDir = new STRawGroupDir(templateUrl, null, '$', '$');
        ST st = stGroupDir.getInstanceOf(templateName);
        st.add("name", name);
        return st.render();
    }

    public static void sendEmail(ArrayList<ContactEmail> emails, String template, String text, String subject) throws MessagingException, MalformedURLException {
        Session session = Session.getDefaultInstance(smtpProps,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpProps.getProperty("mail.username"),smtpProps.getProperty("mail.password"));
                    }
                });

        for(ContactEmail contactEmail: emails){
            System.out.println("try to send " + contactEmail.getEmail());
            String emailText = null;
            if (template != null && !template.isEmpty()){
                emailText = EmailTemplateHelper.getTemplate(template, contactEmail.getContactName());
                System.out.println(emailText);
            }
            MimeMessage message = new MimeMessage(session);
           // message.setFrom(new InternetAddress(smtpProps.getProperty("mail.username")));"liliaqwer@gmail.com"
            message.setFrom(new InternetAddress("liliaqwer@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("andrew.qwer87@gmail.com"));//email
            message.setSubject(subject);
            message.setText(emailText != null ? emailText : text);
            Transport.send(message);
        }
    }
}
