package es.fantasymanager.scheduler.jobs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import es.fantasymanager.data.business.SchedulerTaskData;
import es.fantasymanager.data.enums.JobsEnum;

@Component
public class ScheduleJobBuilder {

    @Autowired
    private SchedulerFactoryBean schedulerFactory;


	public void schedule(SchedulerTaskData task) throws SchedulerException {

		Map<JobDetail, Set<? extends Trigger>> jobs = new HashMap<>();

		JobsEnum jobEnum = task.getJobEnum();
			
		JobDetail job = JobBuilder.newJob(jobEnum.getClazz())
			.withIdentity(task.getUuid().toString(), jobEnum.getName())
			.withDescription(jobEnum.getDescription())
			.usingJobData(task.getJobDataMap())
			.storeDurably(true)
			.build();

        Trigger trigger = TriggerBuilder.newTrigger()
    		.forJob(job)
        	.withIdentity(job.getKey().getName(), jobEnum.getName())
            .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
            .build();

    	HashSet<Trigger> triggers = new HashSet<>();
    	triggers.add(trigger);

		jobs.put(job, triggers);

		// When the task has id it is an update. Otherwise it is new.
		boolean replaceJobs = null != task.getScheduledTaskId();

		this.schedulerFactory.getScheduler().scheduleJobs(jobs, replaceJobs);
	}
}
