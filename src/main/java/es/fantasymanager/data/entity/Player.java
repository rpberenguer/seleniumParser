package es.fantasymanager.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Player  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer playerId;

	@Column(nullable = false, length = 100)
	private String name;

	private String nbaId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEAM_ID")
	private Team team;



}
