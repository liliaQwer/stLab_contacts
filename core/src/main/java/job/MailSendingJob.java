package job;

import email.EmailTemplateHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import service.ContactService;
import service.ContactServiceImpl;
import view.ContactEmail;

import javax.sql.DataSource;
import java.util.ArrayList;

public class MailSendingJob implements Job {
    private final static Logger logger = LogManager.getLogger(MailSendingJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        logger.info("I do my job");
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        DataSource dataSource = (DataSource)data.get("dataSource");
        ContactService contactService = new ContactServiceImpl(dataSource);
        ArrayList<ContactEmail> emailList = null;
        try {
            emailList = (ArrayList<ContactEmail>)contactService.getTodayBirthdayContactsEmails();
            EmailTemplateHelper.sendEmail(emailList, "Birthday", "","Congrats");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            return;
        }
    }
}
