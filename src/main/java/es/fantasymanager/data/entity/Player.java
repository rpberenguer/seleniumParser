package es.fantasymanager.data.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="PLAYER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "statistics")
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

	@OneToMany(mappedBy = "player")
	private List<Statistic> statistics;

}
