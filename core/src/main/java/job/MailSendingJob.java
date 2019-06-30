package job;

import email.EmailTemplateHelper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import service.ContactService;
import service.ContactServiceImpl;
import view.ContactEmail;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;

public class MailSendingJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("I do my job");
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        DataSource dataSource = (DataSource)data.get("dataSource");
        ContactService contactService = new ContactServiceImpl(dataSource);
        ArrayList<ContactEmail> emailList = null;
        try {
            emailList = (ArrayList<ContactEmail>)contactService.getTodayBirthdayContactsEmails();
            EmailTemplateHelper.sendEmail(emailList, "Birthday", "","Congrats");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.err.println("---" + jobExecutionContext.getJobDetail().getKey()
                + " executing.[" + new Date() + "]");
    }
}
