package es.fantasymanager.data.entity;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="GAME")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "statistics")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GAME_ID")
	private Integer gameId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEAM_HOME_ID")
	private Team teamHome;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "TEAM_AWAY_ID")
	private Team teamAway;

	@Column(name = "TEAM_HOME_SCORE")
	private int teamHomeScore;

	@Column(name = "TEAM_AWAY_SCORE")
	private int teamAwayScore;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE")
	private Date date;

	@Column(name = "NBA_ID")
	private String nbaId;

	@OneToMany(mappedBy = "game")
	private List<Statistic> statistics;


}
