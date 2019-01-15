/**
 *
 */
package es.fantasymanager.scheduler.jobs;

import java.io.IOException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.fantasymanager.data.enums.JobsEnum;
import es.fantasymanager.services.TradeParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class TradeParserJob extends AbstractCronJob implements Job {

	private String cronExpression;

	@Autowired
	TradeParserService service;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job {}", this.getClass().getName());

		try {
			JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
			String playerToAdd = jobDataMap.getString("playerToAdd");
			String playerToRemove = jobDataMap.getString("playerToRemove");

			service.doTrade(playerToAdd, playerToRemove);

		} catch (IOException e) {
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
}
