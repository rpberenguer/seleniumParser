package es.fantasymanager.utils;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.openqa.selenium.By;

public interface Constants {

	public static final String URL_ESPN = "http://www.espn.com";
	public static final String URL_ROTOWORLD_NEWS = "https://www.rotoworld.com/basketball/nba/player-news";

	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=511966";
//	public static final String URL_ADD_PLAYERS = "http://fantasy.espn.com/basketball/players/add?leagueId=97189";
	public static final String URL_LEGAUE_ROSTERS = "http://fantasy.espn.com/basketball/league/rosters?leagueId=97189";
	public static final String URL_RECENT_ACTIVITY = "http://fantasy.espn.com/basketball/recentactivity?leagueId=97189";

	/********************************
	 *********** LOGIN **************
	 *******************************/
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

	public static final String SEARCH_PLAYER_TEXT = "div.player--search--matches button[data-player-search-playerid='%s']";

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
	public static final By BY_ROTOWORLD_NEWS_LIST = By
			.cssSelector("ul.player-news__list.active.player-news__dynamic-list li article.player-news-article");
	public static final By BY_ROTOWORLD_NEWS_ARTICLE_DATE = By.cssSelector("div.player-news-article__timestamp");
	public static final By BY_ROTOWORLD_NEWS_ARTICLE_HEADER = By
			.cssSelector("div.player-news-article__header a:nth-of-type(2)");
	public static final By BY_ROTOWORLD_NEWS_ARTICLE_TITLE = By.cssSelector("div.player-news-article__title");
	public static final By BY_ROTOWORLD_NEWS_ARTICLE_SUMMARY = By.cssSelector("div.player-news-article__summary");

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

	/********************************
	 ************ EMOJIS ************
	 ********************************/
	public static final String EMOJI_WARNING = ":warning:";
	public static final Double FANTASYPOINTS_TO_SEND_WARNING = Double.valueOf(15);

	/***************************************
	 ************ FANTASY TEAMS ************
	 ***************************************/
	public static final By BY_FANTASY_TEAM_DIV = By.cssSelector("div[class='pa1 bg-clr-white br-5 roster-container']");
	public static final String PLAYER_IMG_PREFIX = "http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/";
	public static final By BY_FANTASY_TEAM_PLAYER_IMG = By.cssSelector("img[src^='" + PLAYER_IMG_PREFIX + "']");
	public static final By BY_FANTASY_TEAM_TITLE = By.cssSelector("span.teamName.truncate");

	/***************************************
	 ************ TRANSACTIONS *************
	 ***************************************/
	public static final String LAST_TRANSACTION_DATE = "lastTransaction";
	public static final DateTimeFormatter formatterTransaction = new DateTimeFormatterBuilder().parseCaseInsensitive()
			.appendPattern("E MMM d h:mm a").parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
			.toFormatter(Locale.ENGLISH);

	public static final String TRANSACTIONS_START_DATE_OPTIONS = "div.filterDropdowns div.Dropdown__Wrapper.di.nowrap:nth-of-type(2) select option[value='%s']";

	public static final By BY_PAGINATION_NAV_LIST = By.cssSelector("div.PaginationNav__wrap.overflow-x-auto ul li");
	public static final String BY_PAGINATION_NAV_LIST_ELEMENT = "div.PaginationNav__wrap.overflow-x-auto ul li:nth-of-type(%s)";
}
