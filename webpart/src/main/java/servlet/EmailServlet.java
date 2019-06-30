package servlet;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.newJob;

import com.fasterxml.jackson.databind.ObjectMapper;
import email.EmailTemplateHelper;
import job.MailSendingJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import service.ContactService;
import service.ContactServiceImpl;
import utils.ApplicationException;
import view.ContactEmail;
import view.SendEmailData;
import view.EmailTemplates;
import view.Emails;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@WebServlet(urlPatterns = {"/email"}, loadOnStartup=1)
public class EmailServlet extends HttpServlet implements JsonSendable{
    @Resource(name = "jdbc/mySqlDb")
    private DataSource dataSource;
    private Scheduler scheduler;
    private ContactService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("pathInfo=" + pathInfo);
        if ( request.getParameter("ids") != null){
            loadContactsEmail(response, request.getParameter("ids"));
        }else{
            loadEmailTemplates(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(jb.toString());
        ObjectMapper mapper = new ObjectMapper();
        SendEmailData dataToSendEmail = mapper.readValue(jb.toString(), SendEmailData.class);

        try {
            EmailTemplateHelper.sendEmail(dataToSendEmail.getEmailList(),dataToSendEmail.getTemplate(),
                    dataToSendEmail.getText(), dataToSendEmail.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmailTemplates(HttpServletResponse response){
        try {
            EmailTemplates templates = new EmailTemplates(EmailTemplateHelper.getAllTemplates());
            sendJsonResponse(response, templates);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadContactsEmail(HttpServletResponse response, String contactsIdStr){
        System.out.println("contactIdstr= " + contactsIdStr);
        List<Integer> idList = Arrays.stream(contactsIdStr.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        Emails emails = new Emails();
        try {
            for(Integer id: idList){
                String email = service.get(id).getContact().getEmail();
                String contactName = service.get(id).getContact().getName();
                if (email != null){
                    emails.getEmailList().add(new ContactEmail(contactName, email));
                    System.out.println("email=" + email);
                }
            }
            sendJsonResponse(response, emails);
        } catch (ApplicationException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            sendJsonResponse(response, e);
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ContactServiceImpl(dataSource);
        System.out.println("Before doing job");
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
            JobDetail job = newJob(MailSendingJob.class)
                    .withIdentity("job", "group1")
                    .build();
            job.getJobDataMap().put("dataSource", dataSource);
            Date startTime = evenMinuteDate(new Date());
            SimpleTrigger trigger = newTrigger()
                    .withIdentity("trigger", "group1")
                    .startAt(startTime)
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(24*60*60)
                            .repeatForever())
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            System.out.println("Before shut down");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
