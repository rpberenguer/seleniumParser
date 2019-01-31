package es.fantasymanager.utils;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://www.espn.com";
	public static final String URL_ROTOWORLD_NEWS = "http://www.rotoworld.com/playernews/nba/basketball-player-news";

	/******************************
	 *********** TRADE *************
	 *******************************/
//	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=511966";
	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=97189";
	public static final String LOGIN_IFRAME = "disneyid-iframe";

	public static final By BY_EMAIL_INPUT = By.xpath("//input[@type='email']");
	public static final By BY_PASSWORD_INPUT = By.xpath("//input[@type='password']");
	public static final By BY_SUBMIT_LOGIN_BUTTON = By
			.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']");

	public static final String ADD_PLAYER_LINK = ".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "%s.png&w=96&h=70']/../../../../../td[3]/div/div/a";

	public static final String REMOVE_PLAYER_LINK = ".//img[@src='http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/"
			+ "%s.png&w=96&h=70']/../../../../../td[3]/div/div/a";

//	public static final By BY_CONTINUE_TRADE_BUTTON = By
//			.cssSelector("div.flex.justify-between.items-center a:nth-of-type(2)");
	public static final By BY_CONTINUE_TRADE_BUTTON = By
			.cssSelector("div.flex.mt7.pb7.justify-center.items-center a:nth-of-type(2)");
	public static final By BY_SUBMIT_TRADE_BUTTON = By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] ");
	public static final By BY_CONFIRM_TRADE_BUTTON = By.cssSelector("a.btn.btn--custom.btn-confirm-action.mb3");

	/********************************
	 *********** ROSTER *************
	 *******************************/
	public static final String URL_TEAMS = "http://espn.go.com/nba/teams";
	public static final String ROSTER_LINK = "/nba/team/roster/_/name/";
	public static final String PLAYER_LINK = "/nba/player/_/id/";
	public static final By BY_ROSTER_LINK = By.xpath("//a[starts-with(@href, '" + ROSTER_LINK + "')]");
	public static final By BY_PLAYER_LINK = By.xpath(".//a[starts-with(@href, '" + URL_ESPN + PLAYER_LINK + "')]");

	/********************************
	 *********** STATISTIC **********
	 *******************************/
	public static final String URL_SCHEDULE = "http://www.espn.com/nba/schedule/_/date/";
	public static final String TEAM_LINK = "/nba/team/_/name/";
	public static final String GAME_LINK = "/nba/game?gameId=";
	public static final String BOXSCORE_LINK = "/nba/boxscore?gameId=";
	public static final By BY_SCHEDULE_TABLE_DAY = By.cssSelector("table.schedule.has-team-logos.align-left");
	public static final By BY_GAME_LINK = By.xpath(
			".//tbody/tr/td[1]/a[starts-with(@href, '" + TEAM_LINK + "')]/" + "../../td[2]/div/a[starts-with(@href, '"
					+ TEAM_LINK + "')]/../../../td[3]/a[starts-with(@href, '" + GAME_LINK + "')]");

	public static final By BY_TEAM_HOME_DIV = By.cssSelector("div.team.home");
	public static final By BY_TEAM_AWAY_DIV = By.cssSelector("div.team.away");
	public static final By BY_SCORE_HOME_DIV = By.cssSelector("div.score.icon-font-before");
	public static final By BY_SCORE_AWAY_DIV = By.cssSelector("div.score.icon-font-after");
	public static final By BY_TEAM_LINK = By.xpath(".//a[starts-with(@href, '" + TEAM_LINK + "')]");

	public static final By BY_STATISTIC_HOME_ROWS = By
			.cssSelector("div.col.column-two.gamepackage-home-wrap table.mod-data tbody tr");
	public static final By BY_STATISTIC_AWAY_ROWS = By
			.cssSelector("div.col.column-one.gamepackage-away-wrap table.mod-data tbody tr");

	/********************************
	 ************ NEWS **************
	 *******************************/
	public static final By BY_ROTOWORL_NEWS_DIV = By.cssSelector("div.pb");

	/********************************
	 *********** SCHEDULER **********
	 *******************************/
	public static final String CRON_REGEX_VALIDATOR = "^\\s*($|#|\\w+\\s*=|(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|/|,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|/|,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|/|,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|/|,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[01]?\\d|2[0-3])(?:(?:-|/|,)(?:[01]?\\d|2[0-3]))?(?:,(?:[01]?\\d|2[0-3])(?:(?:-|/|,)(?:[01]?\\d|2[0-3]))?)*)\\s+(\\?|\\*|(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|/|,)(?:0?[1-9]|[12]\\d|3[01]))?(?:,(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|/|,)(?:0?[1-9]|[12]\\d|3[01]))?)*)\\s+(\\?|\\*|(?:[1-9]|1[012])(?:(?:-|/|,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|/|,)(?:[1-9]|1[012]))?(?:L|W)?)*|\\?|\\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\\s+(\\?|\\*|(?:[0-6])(?:(?:-|/|,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|/|,|#)(?:[0-6]))?(?:L)?)*|\\?|\\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\\s)+(\\?|\\*|(?:|\\d{4})(?:(?:-|/|,)(?:|\\d{4}))?(?:,(?:|\\d{4})(?:(?:-|/|,)(?:|\\d{4}))?)*))$";

	/********************************
	 *********** ACTIVEMQ **********
	 *******************************/
	public static final String GAME_QUEUE = "game-queue";
	public static final String STATISTIC_QUEUE = "statistic-queue";
	public static final String TRADE_QUEUE = "trade-queue";

}
