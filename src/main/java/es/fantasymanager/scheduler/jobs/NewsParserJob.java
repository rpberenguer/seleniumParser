/**
 *
 */
package es.fantasymanager.scheduler.jobs;

import java.net.MalformedURLException;

import org.quartz.Job;
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
import es.fantasymanager.services.interfaces.NewsParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class NewsParserJob extends AbstractCronJob implements Job {

	@Value("${scheduled.parser.news}")
	private String cronExpression;

	@Autowired
	NewsParserService service;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job {}", this.getClass().getName());

		try {
			service.parseNews();
		} catch (final MalformedURLException e) {
			log.error(e.getMessage(), e);
			throw new JobExecutionException(e.getMessage());
		}
	}

	@Override
	public String getCronExpression() {
		return this.cronExpression;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Job> Class<T> getJobClass() {
		return (Class<T>) NewsParserJob.class;
	}

	@Override
	public String getName() {
		return JobsEnum.NEWS_PARSER.getName();
	}

	@Override
	public String getDescription() {
		return JobsEnum.NEWS_PARSER.getDescription();
	}

	@Bean(name = "newsScheduleBeanJob")
	public JobDetailFactoryBean sampleJob() {
		return QuartzConfiguration.createJobDetail(this.getClass());
	}

	@Bean(name = "newsScheduleBeanTrigger")
	public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("newsScheduleBeanJob") JobDetailFactoryBean jdfb) {
		return QuartzConfiguration.createCronTrigger(jdfb.getObject(), this.getCronExpression());
	}
}
