package es.fantasymanager.data.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.fantasymanager.scheduler.jobs.StatisticParserJob;

@Repository
public class QuartzRepositoryImpl implements QuartzRepository {

	@Autowired
	private transient StatisticParserJob statisticParserJob;

	@Override
	public Map<JobDetail, Set<? extends Trigger>> findJobs() {
		Map<JobDetail, Set<? extends Trigger>> jobs = new HashMap<>();
		jobs.putAll(this.findStatisticParser());
		return jobs;
	}

	private Map<JobDetail, Set<? extends Trigger>> findStatisticParser() {
		Map<JobDetail, Set<? extends Trigger>> jobs = new HashMap<>();
		HashSet<Trigger> triggers = new HashSet<>();
		triggers.add(statisticParserJob.getTrigger());
		jobs.put(statisticParserJob.getJobDetail(), triggers);
		return jobs;
	}

}
