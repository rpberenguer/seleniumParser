package es.fantasymanager.data.business;

import java.io.Serializable;
import java.util.UUID;

import org.quartz.JobDataMap;

import es.fantasymanager.data.enums.JobsEnum;
import lombok.Data;

@Data	  
public class SchedulerTaskData implements Serializable {
		
	private static final long serialVersionUID = -4701052431217858911L;
	
	private Integer scheduledTaskId;
	private UUID uuid;
	
	private JobsEnum jobEnum;
	private String cronExpression;
	
	private JobDataMap jobDataMap;
	
}
