package es.fantasymanager.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEAM_HOME_ID")
	private Team teamHome;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEAM_AWAY_ID")
	private Team teamAway;

	private int teamHomeScore;

	private int teamAwayScore;

	@Temporal(TemporalType.DATE)
	private Date date;

	private String nbaId;

	@OneToMany(mappedBy = "game")
	private List<Statistic> statistics;


}
