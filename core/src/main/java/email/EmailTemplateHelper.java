package email;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.*;
import utils.ApplicationException;
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
    private final static Logger logger = LogManager.getLogger(EmailTemplateHelper.class);
    private static Properties smtpProps;
    static{
        try (InputStream input = EmailTemplateHelper.class.getClassLoader().getResourceAsStream("mail.properties")) {
            smtpProps = new Properties();
            smtpProps.load(input);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    public static ArrayList<TemplateType> getAllTemplates() throws ApplicationException {
        URL urlSlash = EmailTemplateHelper.class.getClassLoader().getResource("templates");
        //URL wrong = Thread.currentThread().getContextClassLoader().getResource("templates");
        FileSystem fileSystem = null;
        ArrayList<TemplateType> templateList = new ArrayList<>();
        try {
            URL templateUrl = new URL(urlSlash.toString().replaceAll("/$", ""));
            STRawGroupDir stGroupDir = new STRawGroupDir(templateUrl, null, '$', '$');

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
                }
            }
            logger.error(e);
            throw new ApplicationException();
        }
        return templateList;
    }

    public static String getTemplate(String templateName, String name) throws ApplicationException {
        URL urlSlash = EmailTemplateHelper.class.getClassLoader().getResource("templates");
        //URL wrong = Thread.currentThread().getContextClassLoader().getResource("templates");
        URL templateUrl = null;
        try {
            templateUrl = new URL(urlSlash.toString().replaceAll("/$", ""));
        } catch (MalformedURLException e) {
           logger.error(e);
           throw new ApplicationException();
        }
        STRawGroupDir stGroupDir = new STRawGroupDir(templateUrl, null, '$', '$');
        ST st = stGroupDir.getInstanceOf(templateName);
        st.add("name", name);
        return st.render();
    }

    public static void sendEmail(ArrayList<ContactEmail> emails, String template, String text, String subject) throws MessagingException, ApplicationException {
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
            }else{
                emailText = text;
            }
            System.out.println("emailText" + emailText);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpProps.getProperty("mail.username")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(contactEmail.getEmail()));
            message.setSubject(subject);
            message.setText(emailText, "UTF-8");
            Transport.send(message);
        }
    }
}
