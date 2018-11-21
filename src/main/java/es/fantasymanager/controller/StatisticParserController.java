package es.fantasymanager.controller;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.StatisticParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class StatisticParserController {

	@Autowired
	private StatisticParserService service;

	@PostMapping(value = "/parser/statistics")
	public String getStatistics() throws MalformedURLException {

		log.info("Inicio parseo staistics");
		LocalDate dateTimeFrom = LocalDate.of(2017, Month.OCTOBER, 22);
		LocalDate dateTimeTo = LocalDate.of(2017, Month.OCTOBER, 30);
		service.getStatistics(dateTimeFrom, dateTimeTo);
		log.info("Fin parseo statistics");

		return "Parseo Statistics OK";
	}
}
