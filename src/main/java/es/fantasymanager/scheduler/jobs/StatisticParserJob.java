/**
 *
 */
package es.fantasymanager.scheduler.jobs;

import java.net.MalformedURLException;
import java.time.LocalDate;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import es.fantasymanager.configuration.quartz.QuartzConfiguration;
import es.fantasymanager.data.enums.JobsEnum;
import es.fantasymanager.services.StatisticParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class StatisticParserJob extends AbstractCronJob implements Job {

	@Value("${scheduled.parser.statistics}")
	private String cronExpression;
	
	@Autowired
	StatisticParserService service;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job {}", this.getClass().getName());

		try {			
			LocalDate startDate = LocalDate.now();
			LocalDate endDate = LocalDate.now();
			
			JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
			if(jobDataMap != null && !jobDataMap.isEmpty()) {
				startDate = (LocalDate) jobDataMap.get("startDate");
				endDate = (LocalDate) jobDataMap.get("endDate");
			}

			service.getStatistics(startDate, endDate);

		} catch (MalformedURLException e) {
			throw new JobExecutionException(e);
		}
	}
	
    @Override
    public String getCronExpression() {
        return this.cronExpression;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Job> Class<T> getJobClass() {
        return (Class<T>) StatisticParserJob.class;
    }
    
	@Override
	public String getName() {
		return JobsEnum.STATISTIC_PARSER.getName();
	}

	@Override
	public String getDescription() {
		return JobsEnum.STATISTIC_PARSER.getDescription();
	} 
	
	@Bean(name = "statisticScheduleBeanJob")
	public JobDetailFactoryBean sampleJob() {
		return QuartzConfiguration.createJobDetail(this.getClass());
	}
	
	@Bean(name = "statisticScheduleBeanTrigger")
	public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("statisticScheduleBeanJob") JobDetailFactoryBean jdfb ) {
		return QuartzConfiguration.createCronTrigger(jdfb.getObject(), this.getCronExpression());
	}
}
