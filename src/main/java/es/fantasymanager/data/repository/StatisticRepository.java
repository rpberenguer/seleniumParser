package es.fantasymanager.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.fantasymanager.data.business.StatisticAvgDto;
import es.fantasymanager.data.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {


	@Query("SELECT new es.fantasymanager.data.business.StatisticAvgDto (p.name as nombre, avg(s.fantasyPoints) as avg, sum(s.fantasyPoints) as tot, avg(s.points) as avgPuntos,"
			+ " avg(s.rebounds) as avgRebotes, avg(s.assists) as avgAsistencias, avg(s.steals) as avgRobos, avg(s.blocks) as avgTapones,"
			+ " avg(s.turnovers) as avgPerdidas) FROM Statistic s JOIN s.player p JOIN s.game g"
			+ " WHERE g.date >= ?1 AND g.date <= ?2 GROUP BY p.name ORDER BY avg(s.fantasyPoints) DESC")
	List<StatisticAvgDto> findAvgAndSumStatisticsByPlayer(Date fromDate, Date toDate);
	
	Statistic findByStatisticId(Integer id);
}