package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.entity.Game;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Statistic;
import es.fantasymanager.data.entity.Team;
import es.fantasymanager.data.repository.GameRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.data.repository.StatisticRepository;
import es.fantasymanager.data.repository.TeamRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticParserServiceImpl implements StatisticParserService, Constants {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Autowired
	private transient GameRepository gameRepository;

	@Autowired
	private transient TeamRepository teamRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient StatisticRepository statisticRepository;
	
	private WebDriver driver;

	@Override
	@Transactional
	public void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws MalformedURLException {

		log.info("Statistic Parser Started! " + Thread.currentThread().getId());

		try {
			driver.get(URL_SCHEDULE + dateTimeFrom.format(formatter));

			// Print the title
			log.debug("Title: {}", driver.getTitle());

			// Tables with all week games
			final List<WebElement> weekGames = driver.findElements(BY_SCHEDULE_TABLE_DAY);

			for (WebElement dayGames : weekGames) {
				List<WebElement> gameLinks = dayGames.findElements(BY_GAME_LINK);
				for (WebElement gameLink : gameLinks) {					
					log.debug("Game link {}", gameLink);
					
					Game game = new Game();
					
					final String gameId = StringUtils.substringAfter(gameLink.getAttribute("href"), GAME_LINK);
					game.setNbaId(gameId);
					
					final Date date = Date.from(dateTimeFrom.atStartOfDay(ZoneId.systemDefault()).toInstant());
					game.setDate(date);
					
					gameRepository.save(game);
				}
				
				// Sumamos un día a la fecha inicial
				dateTimeFrom = dateTimeFrom.plusDays(1);

				// Si hemos superado la fecha fin hemos llegado al final del
				// parseo
				if (dateTimeFrom.isAfter(dateTimeTo)) {
					log.debug("Partidos obtenidos.");
					break;
				}
			}
			
			// Empezamos el parseo de statistics de cada partido
			log.debug("Empezamos parseo de statistics");
			
			List<Game> games = (List<Game>) gameRepository.findAll();
			
			for (Game game : games) {
				// Get Game
				log.debug("GameId {}", game.getNbaId());
				
				driver.get(URL_ESPN + BOXSCORE_LINK + game.getNbaId());
				
				// Team Home
				WebElement teamHomeDiv = driver.findElement(BY_TEAM_HOME_DIV);
				WebElement scoreHomeDiv = teamHomeDiv.findElement(BY_SCORE_HOME_DIV);
				WebElement teamHomeLink = teamHomeDiv.findElement(BY_TEAM_LINK);
				String teamHomeShortCode = StringUtils.substringBetween(teamHomeLink.getAttribute("href"), TEAM_LINK, "/");
				
				Team teamHome = teamRepository.findByShortCode(teamHomeShortCode);

				// Team Away
				WebElement teamAwayDiv = driver.findElement(BY_TEAM_AWAY_DIV);
				WebElement scoreAwayDiv = teamAwayDiv.findElement(BY_SCORE_AWAY_DIV);
				WebElement teamAwayLink = teamAwayDiv.findElement(BY_TEAM_LINK);
				String teamAwayShortCode = StringUtils.substringBetween(teamAwayLink.getAttribute("href"), TEAM_LINK, "/");
				
				Team teamAway = teamRepository.findByShortCode(teamAwayShortCode);
				
				game.setTeamHome(teamHome);
				game.setTeamHome(teamAway);
				game.setTeamAwayScore(Integer.valueOf(scoreAwayDiv.getText()));
				game.setTeamHomeScore(Integer.valueOf(scoreHomeDiv.getText()));
				
				gameRepository.save(game);
				
				// Statistics Home
				List<WebElement> statisticHomeRows = driver.findElements(BY_STATISTIC_HOME_ROWS);
				for (WebElement statisticRow : statisticHomeRows) {
					if (!"highlight".equals(statisticRow.getAttribute("class"))) {
						Statistic statistic = parseStatisticRow((statisticRow));
						if (statistic != null) {
							statistic.setGame(game);
							statistic = statisticRepository.save(statistic);
							log.debug(statistic.toString());
						}
					}
				}
				
				// Statistics Away
				List<WebElement> statisticAwayRows = driver.findElements(BY_STATISTIC_AWAY_ROWS);
				for (WebElement statisticRow : statisticAwayRows) {
					if (!"highlight".equals(statisticRow.getAttribute("class"))) {
						Statistic statistic = parseStatisticRow((statisticRow));
						if (statistic != null) {
							statistic.setGame(game);
							statistic = statisticRepository.save(statistic);
							log.debug(statistic.toString());
						}
					}
				}
			}
			
		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Statistic Parser Ended! " + Thread.currentThread().getId());
	}
	
	private Statistic parseStatisticRow(WebElement statisticRow) {
		Statistic statistic = new Statistic();
		
		WebElement playerLink = statisticRow.findElement(BY_PLAYER_LINK);
		String nbaId = StringUtils.substringAfter(playerLink.getAttribute("href"), PLAYER_LINK);
		
		Player player = playerRepository.findPlayerByNbaId(nbaId);
		if(player == null) {
			player = new Player();
			player.setNbaId(nbaId);
			player.setName(playerLink.findElement(By.cssSelector("span:nth-child(1)")).getText());
			player = playerRepository.save(player);			
		}
		
		statistic.setPlayer(player);
		
		final WebElement minsCell = statisticRow.findElement(By.cssSelector("td:nth-child(2)"));
		if (!"dnp".equals(minsCell.getAttribute("class")) && NumberUtils.isCreatable(minsCell.getText())) {

			// Obtenemos minutos
			final String minutos = minsCell.getText();

			final String[] tirosTotales = statisticRow.findElement(By.cssSelector("td:nth-child(3)")).getText().split("-");
			final String[] tiros3 = statisticRow.findElement(By.cssSelector("td:nth-child(4)")).getText().split("-");
			final String[] tirosLibres = statisticRow.findElement(By.cssSelector("td:nth-child(5)")).getText().split("-");

			Integer tirosTotAnotados = null;
			Integer tirosTotRealizados = null;
			if (tirosTotales.length == 2) {
				tirosTotAnotados = tirosTotales[0] != null && !"".equals(tirosTotales[0])
						? Integer.parseInt(tirosTotales[0]) : 0;
				tirosTotRealizados = tirosTotales[1] != null && !"".equals(tirosTotales[1])
						? Integer.parseInt(tirosTotales[1]) : 0;
			}

			Integer tiros3Anotados = null;
			Integer tiros3Realizados = null;
			if (tiros3.length == 2) {
				tiros3Anotados = tiros3[0] != null && !"".equals(tiros3[0]) ? Integer.parseInt(tiros3[0]) : 0;
				tiros3Realizados = tiros3[1] != null && !"".equals(tiros3[1]) ? Integer.parseInt(tiros3[1]) : 0;
			}

			final Integer tiros2Anotados = tirosTotAnotados - tiros3Anotados;
			final Integer tiros2Realizados = tirosTotRealizados - tiros3Realizados;

			Integer tirosLibresAnotados = new Integer(0);
			Integer tirosLibresRealizados = new Integer(0);
			if (tirosLibres.length == 2) {
				tirosLibresAnotados = tirosLibres[0] != null && !"".equals(tirosLibres[0])
						? Integer.parseInt(tirosLibres[0]) : 0;
				tirosLibresRealizados = tirosLibres[1] != null && !"".equals(tirosLibres[1])
						? Integer.parseInt(tirosLibres[1]) : 0;
			}

			final String rebotes = statisticRow.findElement(By.cssSelector("td:nth-child(8)")).getText();
			final String asistencias = statisticRow.findElement(By.cssSelector("td:nth-child(9)")).getText();
			final String robos = statisticRow.findElement(By.cssSelector("td:nth-child(10)")).getText();
			final String tapones = statisticRow.findElement(By.cssSelector("td:nth-child(11)")).getText();
			final String perdidas = statisticRow.findElement(By.cssSelector("td:nth-child(12)")).getText();
			final String faltas = statisticRow.findElement(By.cssSelector("td:nth-child(13)")).getText();
			final String puntos = statisticRow.findElement(By.cssSelector("td:nth-child(15)")).getText();

			statistic.setMinutes(minutos);
			statistic.setTwoPointersMade(tiros2Anotados);
			statistic.setTwoPointersAttempted(tiros2Realizados);
			statistic.setThreePointersMade(tiros3Anotados);
			statistic.setThreePointersAttempted(tiros3Realizados);
			statistic.setFreeThrowsMade(tirosLibresAnotados);
			statistic.setFreeThrowsAttempted(tirosLibresRealizados);
			statistic.setRebounds(rebotes != null && !"".equals(rebotes) ? Integer.parseInt(rebotes) : 0);
			statistic
					.setAssists(asistencias != null && !"".equals(asistencias) ? Integer.parseInt(asistencias) : 0);
			statistic.setFaults(faltas != null && !"".equals(faltas) ? Integer.parseInt(faltas) : 0);
			statistic.setSteals(robos != null && !"".equals(robos) ? Integer.parseInt(robos) : 0);
			statistic.setTurnovers(perdidas != null && !"".equals(perdidas) ? Integer.parseInt(perdidas) : 0);
			statistic.setBlocks(tapones != null && !"".equals(tapones) ? Integer.parseInt(tapones) : 0);
			statistic.setPoints(puntos != null && !"".equals(puntos) ? Integer.parseInt(puntos) : 0);

			statistic.setFantasyPoints(statistic.calculateFantasyPoints());

			return statistic;
		} else {
			return null;
		}
	}

	@PostConstruct
	private void postConstruct() {
		driver = new ChromeDriver();
	}
}
