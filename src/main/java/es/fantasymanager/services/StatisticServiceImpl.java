package es.fantasymanager.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.business.StatisticAvgDto;
import es.fantasymanager.data.repository.StatisticRepository;
import es.fantasymanager.services.interfaces.StatisticService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

	@Autowired
	private transient StatisticRepository statisticRepository;

	@Override
	@Transactional
	public List<StatisticAvgDto> getStatisticsAvg(LocalDate fromDate, final LocalDate toDate) {

		log.info("Statistic Avg. " + Thread.currentThread().getId());

		return statisticRepository.findAvgAndSumStatisticsByDates(
				Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
				Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

	}

	@Override
	public List<StatisticAvgDto> getStatisticsAvg(LocalDate fromDate, LocalDate toDate, String nbaId) {
		log.info("Statistic Avg. " + Thread.currentThread().getId());

		return statisticRepository.findAvgAndSumStatisticsByDatesAndPlayer(
				Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
				Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), nbaId);

	}
}
