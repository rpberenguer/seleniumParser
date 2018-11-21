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
@Table(name="TEAM")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"players", "gamesHome", "gamesAway"})
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEAM_ID")
	private Integer teamId;

	@Column(name = "NAME", nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "SHORT_CODE", unique = true, length = 4)
	private String shortCode;

	@Column(name = "LONG_CODE", unique = true, length = 50)
	private String longCode;

	@OneToMany(mappedBy = "team")
	private List<Player> players;

	@OneToMany(mappedBy = "teamHome")
	private List<Game> gamesHome;

	@OneToMany(mappedBy = "teamAway")
	private List<Game> gamesAway;

}
