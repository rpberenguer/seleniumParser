package es.fantasymanager.utils;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://fantasy.espn.com/basketball/league/settings?leagueId=511966";
	public static final String URL_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=511966";
	public static final String LOGIN_IFRAME = "disneyid-iframe";

	public static final By BY_EMAIL_INPUT = By.xpath("//input[@type='email']");
	public static final By BY_PASSWORD_INPUT = By.xpath("//input[@type='password']");
	public static final By BY_SUBMIT_LOGIN_BUTTON = By.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']");
	public static final By BY_PLAYERS_TAB = By.cssSelector("li.players.NavSecondary__Item > a.NavSecondary__Link.hasSub");
	public static final By BY_SUBMIT_TRADE_BUTTON = By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] ");
	public static final By BY_CONFIRM_TRADE_BUTTON = By.cssSelector("input[type='submit'][name='confirmBtn'] ");
}
