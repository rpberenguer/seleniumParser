package es.fantasymanager.configuration.quartz;

import java.util.List;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration for Quartz Jobs and Schedulers using spring beans.
 * <p>
 * <i>Note:</i> Never run clustering on separate machines, unless their clocks
 * are synchronized using some form of time-sync service (such as an NTP
 * daemon).
 * </p>
 */
@Slf4j
@Configuration
public class QuartzConfiguration {

	private static final String QUARTZ_PROPERTIES_FILE = "quartz.properties";

	@Autowired
	List<Trigger> listOfTrigger;

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${configuration.quartz.instanceId}")
	private String instanceId;

	@Value("${spring.datasource.url}")
	private String datasourceURL;

	@Value("${spring.datasource.username}")
	private String datasourceUser;

	@Value("${spring.datasource.password}")
	private String datasourcePassword;

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
		log.debug("Configuring Job factory");

		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	/**
	 * Returns Quartz properties that need to be merged on top of quartz.properties
	 * file.
	 * 
	 * @return Returns Quartz properties that need to be merged on top of
	 *         quartz.properties file.
	 */
	public Properties quartzProperties() {
		Properties p = new Properties();
		log.info("Setting Quartz instance id to {}", this.instanceId);
		// We are using random instace ids for auto scaling.
		// TODO Check if this impact missfires or any other feature.
		p.setProperty("org.quartz.scheduler.instanceId", "instance_" + this.instanceId);
		p.setProperty("org.quartz.dataSource.fantasymanager.URL", this.datasourceURL);
		p.setProperty("org.quartz.dataSource.fantasymanager.user", this.datasourceUser);
		p.setProperty("org.quartz.dataSource.fantasymanager.password", this.datasourcePassword);
		return p;
	}

	/**
	 * Creates a Quartz scheduler fully configured with the properties and a job
	 * factory for the specified jobs.
	 * 
	 * @return The scheduler.
	 */
	@Bean
	public SchedulerFactoryBean scheduler() {

		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setConfigLocation(new ClassPathResource(QUARTZ_PROPERTIES_FILE));
		// Overwriting/Decorating configuration values.
		schedulerFactory.setQuartzProperties(quartzProperties());

		schedulerFactory.setOverwriteExistingJobs(true);
		schedulerFactory.setAutoStartup(true);

		// Here we will set all the trigger beans we have defined.
		if (!listOfTrigger.isEmpty()) {
			schedulerFactory.setTriggers(listOfTrigger.toArray(new Trigger[listOfTrigger.size()]));
		}

		log.debug("Setting the Scheduler up");
		schedulerFactory.setJobFactory(springBeanJobFactory());

		return schedulerFactory;
	}
	
	// Use this method for creating cron triggers instead of simple triggers:
	public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
		CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
		factoryBean.setJobDetail(jobDetail);
		factoryBean.setCronExpression(cronExpression);
		factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		return factoryBean;
	}

	public static JobDetailFactoryBean createJobDetail(Class jobClass) {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(jobClass);
		// job has to be durable to be stored in DB:
		factoryBean.setDurability(true);
		return factoryBean;
	}

}
