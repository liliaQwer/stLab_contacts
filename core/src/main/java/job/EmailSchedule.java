package job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.sql.DataSource;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class EmailSchedule {
    DataSource dataSource;
    private Scheduler scheduler;

    public EmailSchedule(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Scheduler start(){
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
            JobDetail job = newJob(MailSendingJob.class)
                    .withIdentity("job", "group1")
                    .build();
            job.getJobDataMap().put("dataSource", dataSource);
            CronTrigger trigger = newTrigger()
                    .withIdentity("trigger", "group1")
                    .withSchedule(cronSchedule("0 0 15 * * ?"))
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }
}
