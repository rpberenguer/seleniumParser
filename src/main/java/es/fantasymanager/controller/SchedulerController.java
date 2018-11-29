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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.TradeRequest;
import es.fantasymanager.data.rest.response.ScheduleResponse;
import es.fantasymanager.scheduler.jobs.TradeParserJob;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SchedulerController {

	@Autowired
	private Scheduler scheduler;

	@PostMapping("/schedule/trade")
	public ResponseEntity<ScheduleResponse> scheduleTrade(@Valid @RequestBody TradeRequest tradeRequest) {
		try {
			JobDetail jobDetail = buildJobDetail(tradeRequest);
			Trigger trigger = buildJobTrigger(jobDetail, tradeRequest);
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

	private JobDetail buildJobDetail(TradeRequest tradeRequest) {
		JobDataMap jobDataMap = new JobDataMap();

		jobDataMap.put("playerToAdd", tradeRequest.getPlayerToAdd());
		jobDataMap.put("playerToRemove", tradeRequest.getPlayerToRemove());

		return JobBuilder.newJob(TradeParserJob.class)
				.withIdentity(UUID.randomUUID().toString(), "trade-jobs")
				.withDescription("Trade Parser Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, TradeRequest tradeRequest) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "trade-triggers")
				.withDescription("Trade Parser Trigger")
				.withSchedule(CronScheduleBuilder.cronSchedule(tradeRequest.getCronExpression()))
				.build();
	}
}


