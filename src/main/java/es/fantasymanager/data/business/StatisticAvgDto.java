package es.fantasymanager.data.business;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class StatisticAvgDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String nbaId;

	private Double fantasyPointsAvg;

	private Double fantasyPointsTot;

	private Double pointsAvg;

	private Double reboundsAvg;

	private Double assistsAvg;

	private Double stealsAg;

	private Double blocksAvg;

	private Double turnoversAvg;

}
