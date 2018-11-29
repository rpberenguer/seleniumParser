/**
 *
 */
package es.fantasymanager.scheduler.jobs;

import java.net.MalformedURLException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.fantasymanager.services.TradeParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class TradeParserJob implements Job {

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

		} catch (MalformedURLException e) {
			throw new JobExecutionException(e);
		}
	}
}
