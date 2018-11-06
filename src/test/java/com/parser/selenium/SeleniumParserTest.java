package com.parser.selenium;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.fantasymanager.services.RosterParserService;
import es.fantasymanager.services.StatisticParserService;
import es.fantasymanager.services.TradeParserService;
import es.fantasymanager.utils.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class SeleniumParserTest implements Constants {

	@Autowired
	private TradeParserService tradeService;
	
	@Autowired
	private RosterParserService rosterService;

	@Autowired
	private StatisticParserService statisticService;
	
	@Test
	public void doTrade() throws MalformedURLException {

		String playerToAdd = "3134907";
		String playerToRemove = "3899663";
		tradeService.doTrade(playerToAdd, playerToRemove);
	}
	
	@Test
	public void getRosters() throws MalformedURLException {

		rosterService.getRosters();
	}

	@Test
	public void getStatistics() throws MalformedURLException {
		
		LocalDate dateTimeFrom = LocalDate.of(2017, Month.OCTOBER, 17);
		LocalDate dateTimeTo = LocalDate.of(2017, Month.OCTOBER, 31);
		statisticService.getStatistics(dateTimeFrom, dateTimeTo);
	}
}
