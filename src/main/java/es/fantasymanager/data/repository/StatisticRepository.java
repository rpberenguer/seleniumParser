package es.fantasymanager.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.fantasymanager.data.business.StatisticAvgDto;
import es.fantasymanager.data.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

	@Query("SELECT new es.fantasymanager.data.business.StatisticAvgDto (p.name, p.nbaId, avg(s.fantasyPoints) as fantasyPointsAvg, sum(s.fantasyPoints) as fantasyPointsTot, avg(s.points) as pointsAvg,"
			+ " avg(s.rebounds) as reboundsAvg, avg(s.assists) as assistsAvg, avg(s.steals) as stealsAvg, avg(s.blocks) as blocksAvg,"
			+ " avg(s.turnovers) as turnoversAvg) FROM Statistic s JOIN s.player p JOIN s.game g"
			+ " WHERE g.date >= ?1 AND g.date <= ?2 GROUP BY p.name, p.nbaId ORDER BY avg(s.fantasyPoints) DESC")
	List<StatisticAvgDto> findAvgAndSumStatisticsByDates(Date fromDate, Date toDate);

	@Query("SELECT new es.fantasymanager.data.business.StatisticAvgDto (p.name, p.nbaId, avg(s.fantasyPoints) as fantasyPointsAvg, sum(s.fantasyPoints) as fantasyPointsTot, avg(s.points) as pointsAvg,"
			+ " avg(s.rebounds) as reboundsAvg, avg(s.assists) as assistsAvg, avg(s.steals) as stealsAvg, avg(s.blocks) as blocksAvg,"
			+ " avg(s.turnovers) as turnoversAvg) FROM Statistic s JOIN s.player p JOIN s.game g"
			+ " WHERE g.date >= ?1 AND g.date <= ?2 AND p.nbaId = ?3")
	List<StatisticAvgDto> findAvgAndSumStatisticsByDatesAndPlayer(Date fromDate, Date toDate, String nbaId);

	Statistic findByStatisticId(Integer id);
}