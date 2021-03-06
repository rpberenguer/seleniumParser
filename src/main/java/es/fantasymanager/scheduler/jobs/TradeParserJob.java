/**
 *
 */
package es.fantasymanager.scheduler.jobs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.fantasymanager.data.enums.JobsEnum;
import es.fantasymanager.services.interfaces.TradeParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class TradeParserJob extends AbstractCronJob implements Job {

	private String cronExpression;

	@Autowired
	private transient TradeParserService service;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job {}", this.getClass().getName());

		try {
			JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
			Map<String, String> tradeMap = (HashMap<String, String>) jobDataMap.get("tradeMap");
			LocalDateTime tradeDate = (LocalDateTime) jobDataMap.get("tradeDate");

			service.doTrade(tradeMap, tradeDate);

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
