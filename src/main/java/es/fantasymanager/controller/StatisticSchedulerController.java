package es.fantasymanager.controller;

import javax.validation.Valid;

import org.quartz.JobDataMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.data.rest.request.StatisticRequest;

@RestController
public class StatisticSchedulerController extends SchedulerController {


	@PostMapping("/statistic")
	public void scheduleTrade(@Valid @RequestBody StatisticRequest statisticRequest) {
		super.schedule(statisticRequest);
	}


	@Override
	public JobDataMap getJobDataMap(BaseSheduledCronJobRequest request) {
		
		StatisticRequest statisticRequest = (StatisticRequest) request;
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("startDate", statisticRequest.getStartDate());
		jobDataMap.put("endDate", statisticRequest.getEndDate());

		return jobDataMap;
	}
}


