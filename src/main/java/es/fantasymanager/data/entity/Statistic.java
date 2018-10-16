package es.fantasymanager.data.entity;

import javax.persistence.CascadeType;
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
public class Statistic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pkid;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PLAYER_ID")
	private Player player;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "GAME_ID")
	private Game game;

	private String minutes;
	private Integer points;
	private Integer rebounds;
	private Integer assists;
	private Integer steals;
	private Integer blocks;
	private Integer faults;
	private Integer turnovers;
	private Double fantasyPoints;
	private Integer twoPointersMade;
	private Integer twoPointersAttempted;
	private Integer threePointersMade;
	private Integer threePointersAttempted;
	private Integer freeThrowsMade;
	private Integer freeThrowsAttempted;

	public Double calculateFantasyPoints() {
		final Integer twoPointersMissed = twoPointersAttempted - twoPointersMade;
		final Integer threePointersMissed = threePointersAttempted - threePointersMade;
		final Integer freeThrowsMissed = freeThrowsAttempted - freeThrowsMade;

		final Double fantasyPoints = new Double(points + rebounds + assists + steals + blocks - turnovers
				- twoPointersMissed - threePointersMissed * 1.5 - freeThrowsMissed * 0.5);

		return fantasyPoints;
	}

}