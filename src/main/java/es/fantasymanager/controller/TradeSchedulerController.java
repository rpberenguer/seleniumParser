package es.fantasymanager.controller;

import javax.validation.Valid;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.data.rest.request.TradeRequest;
import es.fantasymanager.data.rest.response.ScheduleResponse;
import es.fantasymanager.scheduler.jobs.TradeParserJob;

@RestController
public class TradeSchedulerController extends SchedulerController {


	@PostMapping("/trade")
	public ResponseEntity<ScheduleResponse> scheduleTrade(@Valid @RequestBody TradeRequest tradeRequest) {
		return super.schedule(tradeRequest);
	}


	@Override
	public JobDataMap getJobDataMap(BaseSheduledCronJobRequest request) {
		
		TradeRequest tradeRequest = (TradeRequest) request;
		JobDataMap jobDataMap = new JobDataMap();

		jobDataMap.put("playerToAdd", tradeRequest.getPlayerToAdd());
		jobDataMap.put("playerToRemove", tradeRequest.getPlayerToRemove());

		return jobDataMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Job> Class<T> getJobClass() {
		return (Class<T>) TradeParserJob.class;
	}
}


