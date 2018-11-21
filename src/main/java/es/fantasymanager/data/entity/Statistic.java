package es.fantasymanager.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="STATISTIC")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Statistic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STATISTIC_ID")
	private Integer statisticId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "PLAYER_ID")
	private Player player;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "GAME_ID")
	private Game game;

	@Column(name = "MINUTES")
	private String minutes;

	@Column(name = "POINTS")
	private Integer points;

	@Column(name = "REBOUNDS")
	private Integer rebounds;

	@Column(name = "ASSISTS")
	private Integer assists;

	@Column(name = "STEALS")
	private Integer steals;

	@Column(name = "BLOCKS")
	private Integer blocks;

	@Column(name = "FAULTS")
	private Integer faults;

	@Column(name = "TOURNOVERS")
	private Integer turnovers;

	@Column(name = "FANTASY_POINTS")
	private Double fantasyPoints;

	@Column(name = "TWO_POINTERS_MADE")
	private Integer twoPointersMade;

	@Column(name = "TWO_POINTERS_ATTEMPTED")
	private Integer twoPointersAttempted;

	@Column(name = "THREE_POINTERS_MADE")
	private Integer threePointersMade;

	@Column(name = "THREE_POINTERS_ATTEMPTED")
	private Integer threePointersAttempted;

	@Column(name = "FREE_THROWS_MADE")
	private Integer freeThrowsMade;

	@Column(name = "FREE_THROWS_ATTEMPTED")
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