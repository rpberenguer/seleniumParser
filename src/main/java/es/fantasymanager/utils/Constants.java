package es.fantasymanager.utils;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://fantasy.espn.com/basketball/league/settings?leagueId=511966";
	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=511966";
	public static final String LOGIN_IFRAME = "disneyid-iframe";

	public static final By BY_EMAIL_INPUT = By.xpath("//input[@type='email']");
	public static final By BY_PASSWORD_INPUT = By.xpath("//input[@type='password']");
	public static final By BY_SUBMIT_LOGIN_BUTTON = By.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']");
	public static final By BY_PLAYERS_TAB = By.cssSelector("li.players.NavSecondary__Item > a.NavSecondary__Link.hasSub");
	public static final String IMG_PLAYER_PREFIX = "http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/";
	public static final String IMG_PLAYER_SUFIX = ".png&w=96&h=70";
	
	public static final By BY_ADD_PLAYER_LINK = By.xpath(".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "3423.png&w=96&h=70']/../../../../../td[3]/div/div/a");	
	
	public static final By BY_REMOVE_PLAYER_LINK = By.xpath(".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "2995702.png&w=96&h=70']/../../../../../td[3]/div/div/a");
	
	public static final By BY_CONTINUE_TRADE_BUTTON = By.cssSelector("div.flex.justify-between.items-center a:nth-of-type(2)");
	
	public static final By BY_SUBMIT_TRADE_BUTTON = By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] ");
	public static final By BY_CONFIRM_TRADE_BUTTON = By.cssSelector("a.btn.btn--custom.btn-confirm-action.mb3");
}
