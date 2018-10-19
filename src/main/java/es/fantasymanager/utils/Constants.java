package es.fantasymanager.utils;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://www.espn.com";
	public static final String URL_ESPN_FANTASY = "http://fantasy.espn.com/basketball/league/settings?leagueId=511966";
	
	/******************************
	*********** TRADE *************
	*******************************/
	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=511966";
	public static final String LOGIN_IFRAME = "disneyid-iframe";

	public static final By BY_EMAIL_INPUT = By.xpath("//input[@type='email']");
	public static final By BY_PASSWORD_INPUT = By.xpath("//input[@type='password']");
	public static final By BY_SUBMIT_LOGIN_BUTTON = By.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']");
	
	public static final String ADD_PLAYER_LINK = ".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "%s.png&w=96&h=70']/../../../../../td[3]/div/div/a";	
	
	public static final String REMOVE_PLAYER_LINK = ".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "%s.png&w=96&h=70']/../../../../../td[3]/div/div/a";
	
	public static final By BY_CONTINUE_TRADE_BUTTON = By.cssSelector("div.flex.justify-between.items-center a:nth-of-type(2)");
	public static final By BY_SUBMIT_TRADE_BUTTON = By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] ");
	public static final By BY_CONFIRM_TRADE_BUTTON = By.cssSelector("a.btn.btn--custom.btn-confirm-action.mb3");
	
	/******************************
	*********** ROSTER *************
	*******************************/
	public static final String URL_TEAMS = "http://espn.go.com/nba/teams";
	public static final String TEAM_LINK = "/nba/team/roster/_/name/";
	public static final String PLAYER_LINK = "/nba/player/_/id/";
	public static final By BY_TEAM_LINK = By.xpath("//a[starts-with(@href, '" + TEAM_LINK + "')]");
	public static final By BY_PLAYER_LINK = By.xpath("//a[starts-with(@href, '" + URL_ESPN + PLAYER_LINK + "')]");
	
	
	/******************************
	*********** STATISTIC *************
	*******************************/
	public static final String URL_SCHEDULE = "http://www.espn.com/nba/schedule/_/date/";
	public static final By BY_SCHEDULE_TABLE_DAY = By.cssSelector("table.schedule has-team-logos align-left");
	public static final By BY_GAME_LINK = By.cssSelector("a[id$='/nba/game?gameId=']");
}
