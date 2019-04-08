package es.fantasymanager.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "FANTASY_TEAM")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "players")
public class FantasyTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FANTASY_TEAM_ID")
	private Integer fantasyTeamId;

	@Column(name = "TEAM_NAME", length = 500)
	private String teamName;

	@Column(name = "ABBREV", length = 5)
	private String abbrev;

	@OneToMany(mappedBy = "fantasyTeam")
	private List<Player> players;

}