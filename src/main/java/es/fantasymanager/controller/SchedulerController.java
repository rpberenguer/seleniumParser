package es.fantasymanager.controller;

import java.util.function.Function;

import javax.validation.Valid;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.business.SchedulerTaskData;
import es.fantasymanager.data.enums.JobsEnum;
import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.services.interfaces.SchedulerService;

@RestController
@RequestMapping(path = "/schedule")
public abstract class SchedulerController {

	@Autowired
    private SchedulerService schedulerService;

	public abstract JobDataMap getJobDataMap(BaseSheduledCronJobRequest request);

	public void schedule(@Valid @RequestBody BaseSheduledCronJobRequest request) {
		
		try {
			schedulerService.scheduleTask(this.requestToData.apply(request));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
    Function<BaseSheduledCronJobRequest, SchedulerTaskData> requestToData = input -> {
    	SchedulerTaskData output = new SchedulerTaskData();
    	
    	JobDataMap jobDataMap = getJobDataMap(input);
    	output.setJobDataMap(jobDataMap);
    	output.setJobEnum(JobsEnum.fromName(input.getJobName()));
    	output.setCronExpression(input.getCronExpression());
    	
		return output;
    };
}


