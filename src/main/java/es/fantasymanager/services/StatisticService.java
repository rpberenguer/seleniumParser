package es.fantasymanager.services;

import java.time.LocalDate;
import java.util.List;

import es.fantasymanager.data.business.StatisticAvgDto;

public interface StatisticService {

	public List<StatisticAvgDto> getStatisticsAvg(LocalDate fromDate, final LocalDate toDate);

	public List<StatisticAvgDto> getStatisticsAvg(LocalDate fromDate, final LocalDate toDate, final String nbaId);
}
