package es.fantasymanager.data.repository;

import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.Trigger;

@FunctionalInterface
public interface QuartzRepository {
	
	Map<JobDetail, Set<? extends Trigger>> findJobs();

}
