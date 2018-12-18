package es.fantasymanager.data.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "SCHEDULED_TASK")
@EqualsAndHashCode(callSuper = false)
public class ScheduledTaskEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULED_TASK_ID")
	private Integer scheduledTaskId;
	
	@NotNull
	@Column(name = "UUID")
	@Type(type = "uuid-binary")
	private UUID uuid;

	@NotNull
	@Column(name = "JOB_NAME")
	private String jobName;
	
	@NotNull
	@Column(name = "DESCRIPTION")
	private String description;
	
}
