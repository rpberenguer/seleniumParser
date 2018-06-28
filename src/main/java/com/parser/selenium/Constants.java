package com.parser.selenium;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://games.espn.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=511966";
	public static final String LOGIN_IFRAME = "disneyid-iframe";

	public static final By BY_EMAIL_INPUT = By.xpath("//input[@type='email']");
	public static final By BY_PASSWORD_INPUT = By.xpath("//input[@type='password']");
	public static final By BY_SUBMIT_LOGIN_BUTTON = By.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']");
	public static final By BY_PLAYERS_TAB = By.cssSelector("#games-tabs li:nth-of-type(3) > a");
	public static final By BY_SUBMIT_TRADE_BUTTON = By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] ");
	public static final By BY_CONFIRM_TRADE_BUTTON = By.cssSelector("input[type='submit'][name='confirmBtn'] ");
}
