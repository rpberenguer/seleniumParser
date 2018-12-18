package es.fantasymanager.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import es.fantasymanager.data.entity.ScheduledTaskEntity;

public interface ScheduledTasksRepository extends JpaRepository<ScheduledTaskEntity, Integer> {
	
	ScheduledTaskEntity findByUuid(UUID uuid);
	ScheduledTaskEntity findByScheduledTaskId(Integer id);	
	ScheduledTaskEntity findByJobName(String jobName);
    
}
