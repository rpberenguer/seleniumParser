package es.fantasymanager.controller;

import javax.validation.Valid;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.data.rest.request.StatisticRequest;
import es.fantasymanager.data.rest.response.ScheduleResponse;
import es.fantasymanager.scheduler.jobs.StatisticParserJob;

@RestController
public class StatisticSchedulerController extends SchedulerController {


	@PostMapping("/statistic")
	public ResponseEntity<ScheduleResponse> scheduleTrade(@Valid @RequestBody StatisticRequest statisticRequest) {
		return super.schedule(statisticRequest);
	}


	@Override
	public JobDataMap getJobDataMap(BaseSheduledCronJobRequest request) {
		
		StatisticRequest statisticRequest = (StatisticRequest) request;
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("startDate", statisticRequest.getStartDate());
		jobDataMap.put("endDate", statisticRequest.getEndDate());

		return jobDataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Job> Class<T> getJobClass() {
		return (Class<T>) StatisticParserJob.class;
	}
}


