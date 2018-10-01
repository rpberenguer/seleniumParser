package com.parser.selenium;

import java.net.MalformedURLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.fantasymanager.services.TradeParserService;
import es.fantasymanager.utils.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class SeleniumGridDockerParser implements Constants {

	@Autowired
	private TradeParserService tradeService;
	
	@Test
	public void doThese() throws MalformedURLException {

		String playerToAdd = "3134907";
		String playerToRemove = "3899663";
		tradeService.doTrade(playerToAdd, playerToRemove);
	}
}
