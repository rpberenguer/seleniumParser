package es.fantasymanager.services;

import java.util.UUID;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.fantasymanager.data.business.SchedulerTaskData;
import es.fantasymanager.data.entity.ScheduledTaskEntity;
import es.fantasymanager.data.enums.JobsEnum;
import es.fantasymanager.data.repository.QuartzRepository;
import es.fantasymanager.data.repository.ScheduledTasksRepository;
import es.fantasymanager.scheduler.jobs.ScheduleJobBuilder;
import es.fantasymanager.services.interfaces.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Autowired
    private ScheduleJobBuilder scheduleJobBuilder;

    @Autowired
    private QuartzRepository quartzRepository;

    @Autowired
    private ScheduledTasksRepository scheduledTasksRepository;


	@PostConstruct
	private void initialSchedule() throws SchedulerException {
		// Disable jobs created with hard-code.
//		this.scheduleJobs(true);
	}

	/**
	 * Schedules the prepared jobs.
	 * In a clustered environment the scheduler must not be schedule more than once. To avoid this
	 * problem, the application is prepared to replace previous existing schedules for the aready existing jobs.
	 * @param force forces the scheduler to replace the jobs if they previously exist.
	 * @throws SchedulerException
	 */
    @Override
    public void scheduleJobs(boolean force) throws SchedulerException {
    	// TODO Check if the jobs had been previously defined and update only in a forced execution
        schedulerFactory.getScheduler().scheduleJobs(quartzRepository.findJobs(), true);
    }

    @Transactional
    @Override
    public SchedulerTaskData scheduleTask(SchedulerTaskData task) throws SchedulerException {

    	ScheduledTaskEntity entity = this.dataToEntity.apply(task);
    	entity.setUuid(UUID.randomUUID());
    	entity = scheduledTasksRepository.save(entity);

    	task.setUuid(entity.getUuid());
//    	task.setScheduledTaskId(entity.getScheduledTaskId());

    	// The task must be scheduled at the end to guarantee transaction roll back.
    	this.scheduleJobBuilder.schedule(task);
    	return task;
    }

    @Transactional
    @Override
    public SchedulerTaskData updateScheduledTask(SchedulerTaskData task) throws SchedulerException {

    	ScheduledTaskEntity entity = scheduledTasksRepository.findByUuid(task.getUuid());
    	entity.setDescription(task.getJobEnum().getDescription());
    	entity = scheduledTasksRepository.save(entity);

    	// The task name must be the previous one to avoid duplicating jobs and triggers.
    	task.setUuid(entity.getUuid());
    	task.setScheduledTaskId(entity.getScheduledTaskId());
    	this.scheduleJobBuilder.schedule(task);
    	return task;
    }

    @Transactional
    @Override
	public void scheduleTask(UUID taskUuid, String cronExpression) throws SchedulerException {
    	ScheduledTaskEntity entity = scheduledTasksRepository.findByUuid(taskUuid);
		if (null == entity) {
//			throw new UncheckedException(ErrorType.TASK_NOT_FOUND);
		}

		SchedulerTaskData task = this.entityToData.apply(entity);
		task.setCronExpression(cronExpression);
    	this.scheduleJobBuilder.schedule(task);
	}

	@Transactional(readOnly = true)
    @Override
    public SchedulerTaskData findScheduleTaskByJobName(String jobName) {
    	ScheduledTaskEntity entity = scheduledTasksRepository.findByJobName(jobName);

    	SchedulerTaskData task = null;
    	if (null != entity) {
    		task = this.entityToData.apply(entity);
    	}
    	return task;
    }

    Function<SchedulerTaskData, ScheduledTaskEntity> dataToEntity = input -> {
    	ScheduledTaskEntity output = new ScheduledTaskEntity();
    	output.setUuid(input.getUuid());
    	output.setScheduledTaskId(input.getScheduledTaskId());
    	output.setJobName(input.getJobEnum().getName());
    	output.setDescription(input.getJobEnum().getDescription());
		return output;
    };

    Function<ScheduledTaskEntity, SchedulerTaskData> entityToData = input -> {
    	SchedulerTaskData output = new SchedulerTaskData();
    	output.setUuid(input.getUuid());
    	output.setScheduledTaskId(input.getScheduledTaskId());
    	output.setJobEnum(JobsEnum.fromName(input.getJobName()));
		return output;
    };

}
