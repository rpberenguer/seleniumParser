package es.fantasymanager.scheduler.jobs;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import es.fantasymanager.configuration.quartz.QuartzConfiguration;

/**
 * Abstract class for cron jobs that wrap the job execution, job details and job scheduler creation.
 * @author mtablado
 */
public abstract class AbstractCronJob implements Job {

    private JobDetail jobDetail = null;
    private Trigger trigger = null;

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getCronExpression();
    public abstract <T extends Job> Class<T> getJobClass();

    /**
     * Creates and builds a trigger for the job based on it jobs details and a cron expression
     * that must be specified.
     * This method must be final but it is not because of testing mocks. Do not override!
     */
    public Trigger getTrigger() {
        if (null == this.trigger) {
            this.trigger = TriggerBuilder.newTrigger().forJob(this.getJobDetail())
            	.withIdentity(this.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(this.getCronExpression()))
                .build();
        }
        return this.trigger;
    }

    /**
     * Creates a job detail in case it has not been previously created.
     * This method must be final but it is not because of testing mocks. Do not override!
     * @return The resulting job detail.
     */
    public JobDetail getJobDetail() {
        // TODO Add parameters.
        if (null == this.jobDetail) {
            JobDetailImpl jd = new JobDetailImpl();
            jd.setJobClass(this.getJobClass());
            jd.setName(this.getName());
            jd.setDescription(this.getDescription());
            jd.setDurability(true);
            this.jobDetail = jd;
        }
        return this.jobDetail;
    }
}
