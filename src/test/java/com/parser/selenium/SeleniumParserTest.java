package com.parser.selenium;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
//@ContextConfiguration(classes = { AppConfig.class })
public class SeleniumParserTest implements Constants {

//	@Autowired
//	private TradeParserService tradeService;
//
//	@Autowired
//	private RosterParserService rosterService;
//
//	@Autowired
//	private StatisticParserService statisticService;

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("d MMM. yyyy HH:mm")
			.toFormatter(Locale.getDefault());

	@Test
	public void doTrade() throws IOException {

		String playerToAdd = "3134907";
		String playerToRemove = "3899663";
//		tradeService.doTrade(playerToAdd, playerToRemove);
	}

	@Test
	public void getRosters() throws MalformedURLException {

//		rosterService.getRosters();
	}

	@Test
	public void getStatistics() throws MalformedURLException {

//		LocalDate dateTimeFrom = LocalDate.of(2017, Month.OCTOBER, 17);
//		LocalDate dateTimeTo = LocalDate.of(2017, Month.OCTOBER, 31);
//		statisticService.getStatistics(dateTimeFrom, dateTimeTo);
	}

	@Test
	public void parseNews() throws MalformedURLException {

		String strDate = "15 mar. 2019 22:59";
		LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
		log.info("date {}, datTime {}", strDate, dateTime);
	}
}
