package es.fantasymanager.services.interfaces;


import java.util.UUID;

import org.quartz.SchedulerException;

import es.fantasymanager.data.business.SchedulerTaskData;

public interface SchedulerService {

	/**
	 * Schedules the prepared jobs.
	 * @param force forces the scheduler to replace the jobs it they previously exist.
	 * @throws SchedulerException
	 */
    void scheduleJobs(boolean force) throws SchedulerException;

    SchedulerTaskData scheduleTask(SchedulerTaskData task) throws SchedulerException;

    SchedulerTaskData updateScheduledTask(SchedulerTaskData task) throws SchedulerException;

    void scheduleTask(UUID taskUuid, String cronExpression) throws SchedulerException;

    SchedulerTaskData findScheduleTaskByJobName(String jobName);

}
