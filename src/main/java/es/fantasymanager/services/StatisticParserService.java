package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDate;

public interface StatisticParserService {

	public void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws MalformedURLException;
}
