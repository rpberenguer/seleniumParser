package es.fantasymanager.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.data.rest.response.ScheduleResponse;
import es.fantasymanager.scheduler.jobs.TradeParserJob;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/schedule")
@Slf4j
public abstract class SchedulerController {

	@Autowired
	private Scheduler scheduler;

	public abstract JobDataMap getJobDataMap(BaseSheduledCronJobRequest request);

	public ResponseEntity<ScheduleResponse> schedule(@Valid @RequestBody BaseSheduledCronJobRequest request) {
		try {
			JobDetail jobDetail = buildJobDetail(request);
			Trigger trigger = buildJobTrigger(jobDetail, request);
			scheduler.scheduleJob(jobDetail, trigger);

			ScheduleResponse scheduleResponse = new ScheduleResponse(true,
					jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "Trade Scheduled Successfully!");
			return ResponseEntity.ok(scheduleResponse);

		} catch (SchedulerException ex) {
			log.error("Error scheduling trade", ex);

			ScheduleResponse scheduleEmailResponse = new ScheduleResponse(false,
					"Error scheduling trade. Please try later!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
		}
	}

	private JobDetail buildJobDetail(BaseSheduledCronJobRequest request) {

		JobDataMap jobDataMap = getJobDataMap(request);

		return JobBuilder.newJob(TradeParserJob.class)
				.withIdentity(UUID.randomUUID().toString(), "trade-jobs")
				.withDescription("Trade Parser Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, BaseSheduledCronJobRequest request) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "trade-triggers")
				.withDescription("Trade Parser Trigger")
				.withSchedule(CronScheduleBuilder.cronSchedule(request.getCronExpression()))
				.build();
	}
}


