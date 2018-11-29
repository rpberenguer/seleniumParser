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
import org.springframework.stereotype.Component;

import es.fantasymanager.services.StatisticParserService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Component
public class StatisticParserJob implements Job {

	@Autowired
	StatisticParserService service;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("Executing Job {}", this.getClass().getName());

		try {
			JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
			String dateTimeFrom = jobDataMap.getString("dateTimeFrom");
			String dateTimeTo = jobDataMap.getString("dateTimeTo");

			service.getStatistics(LocalDate.parse(dateTimeFrom), LocalDate.parse(dateTimeTo));;

		} catch (MalformedURLException e) {
			throw new JobExecutionException(e);
		}
	}
}
