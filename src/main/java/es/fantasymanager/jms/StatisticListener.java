package es.fantasymanager.jms;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import es.fantasymanager.data.entity.Game;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Statistic;
import es.fantasymanager.data.entity.Team;
import es.fantasymanager.data.jms.StatisticJmsMessageData;
import es.fantasymanager.data.repository.GameRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.data.repository.StatisticRepository;
import es.fantasymanager.data.repository.TeamRepository;
import es.fantasymanager.services.interfaces.TelegramService;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StatisticListener implements Constants {

	@Autowired
	private transient GameRepository gameRepository;

	@Autowired
	private transient TeamRepository teamRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient StatisticRepository statisticRepository;

	@Autowired
	private transient TelegramService telegramService;

	@JmsListener(destination = STATISTIC_QUEUE)
	public void receiveMessage(@Payload StatisticJmsMessageData statisticMessage, @Headers MessageHeaders headers,
			Message message, Session session) throws JMSException, IOException {

		log.info("Received <---" + statisticMessage + "--->");

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);

		// Timing
		long startTimeInSec = Instant.now().getEpochSecond();

		try {
			for (String gameId : statisticMessage.getGameIds()) {
				log.info("GameId {}", gameId);

				driver.get(URL_ESPN + BOXSCORE_LINK + gameId);

//			// Team Home
//		WebElement teamHomeDiv = waitFor(wait, ExpectedConditions.visibilityOfElementLocated(BY_TEAM_HOME_DIV), "Team Home not found correctly");
//		WebElement teamHomeDiv = driver.findElement(BY_TEAM_HOME_DIV);
				WebElement teamHomeDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(BY_TEAM_HOME_DIV));
				WebElement scoreHomeDiv = teamHomeDiv.findElement(BY_SCORE_HOME_DIV);
				WebElement teamHomeLink = teamHomeDiv.findElement(BY_TEAM_LINK);
				String teamHomeShortCode = StringUtils.substringBetween(teamHomeLink.getAttribute("href"), TEAM_LINK,
						"/");

				Team teamHome = teamRepository.findByShortCode(teamHomeShortCode);
				log.info("Team Home {}", teamHome);

				// Team Away
//	WebElement teamAwayDiv = waitFor(wait, ExpectedConditions.visibilityOfElementLocated(BY_TEAM_AWAY_DIV), "Team Away not found correctly");
				WebElement teamAwayDiv = driver.findElement(BY_TEAM_AWAY_DIV);
				WebElement scoreAwayDiv = teamAwayDiv.findElement(BY_SCORE_AWAY_DIV);
				WebElement teamAwayLink = teamAwayDiv.findElement(BY_TEAM_LINK);
				String teamAwayShortCode = StringUtils.substringBetween(teamAwayLink.getAttribute("href"), TEAM_LINK,
						"/");

				Team teamAway = teamRepository.findByShortCode(teamAwayShortCode);
				log.info("Team Away {}", teamAway);

				Game game = gameRepository.findByNbaId(gameId);
				game.setTeamHome(teamHome);
				game.setTeamAway(teamAway);
				game.setTeamAwayScore(Integer.valueOf(scoreAwayDiv.getText()));
				game.setTeamHomeScore(Integer.valueOf(scoreHomeDiv.getText()));

				gameRepository.save(game);

				// Statistics Home
//		List<WebElement> statisticHomeRows =  waitFor(wait, ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_STATISTIC_HOME_ROWS), "Statistic Home Rows not found correctly");
				List<WebElement> statisticHomeRows = driver.findElements(BY_STATISTIC_HOME_ROWS);
				for (WebElement statisticRow : statisticHomeRows) {
					if (!"highlight".equals(statisticRow.getAttribute("class"))) {
						Statistic statistic = parseStatisticRow(statisticRow);
						if (statistic != null) {
							statistic.setGame(game);
							statistic = statisticRepository.save(statistic);
							log.debug("Statistic Home {}", statistic);
						}
					}
				}

				// Statistics Away
//		List<WebElement> statisticAwayRows =  waitFor(wait, ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_STATISTIC_AWAY_ROWS), "Statistic Away Rows not found correctly");
				List<WebElement> statisticAwayRows = driver.findElements(BY_STATISTIC_AWAY_ROWS);
				for (WebElement statisticRow : statisticAwayRows) {
					if (!"highlight".equals(statisticRow.getAttribute("class"))) {
						Statistic statistic = parseStatisticRow(statisticRow);
						if (statistic != null) {
							statistic.setGame(game);
							statistic = statisticRepository.save(statistic);
							log.debug("Statistic Away {}", statistic);
						}
					}
				}
			}

		} finally {
			// Cerramos driver
			driver.close();
		}

		Long endTimeInSec = Instant.now().getEpochSecond();

		// Logeamos + telegram
		String text = String.format("Estadisticas obtenidas para los partidos %s. Tiempo %s segs.",
				statisticMessage.getGameIds(), endTimeInSec - startTimeInSec);
		log.info(text);
		telegramService.sendMessage(text);
	}

	private Statistic parseStatisticRow(WebElement statisticRow) {

		Statistic statistic = new Statistic();

		WebElement playerLink = statisticRow.findElement(BY_PLAYER_LINK);
		String nbaId = StringUtils.substringAfter(playerLink.getAttribute("href"), PLAYER_LINK);

		Player player = playerRepository.findPlayerByNbaId(nbaId);
		if (player == null) {
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

			final String[] tirosTotales = statisticRow.findElement(By.cssSelector("td:nth-child(3)")).getText()
					.split("-");
			final String[] tiros3 = statisticRow.findElement(By.cssSelector("td:nth-child(4)")).getText().split("-");
			final String[] tirosLibres = statisticRow.findElement(By.cssSelector("td:nth-child(5)")).getText()
					.split("-");

			Integer tirosTotAnotados = null;
			Integer tirosTotRealizados = null;
			if (tirosTotales.length == 2) {
				tirosTotAnotados = tirosTotales[0] != null && !"".equals(tirosTotales[0])
						? Integer.parseInt(tirosTotales[0])
						: 0;
				tirosTotRealizados = tirosTotales[1] != null && !"".equals(tirosTotales[1])
						? Integer.parseInt(tirosTotales[1])
						: 0;
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
						? Integer.parseInt(tirosLibres[0])
						: 0;
				tirosLibresRealizados = tirosLibres[1] != null && !"".equals(tirosLibres[1])
						? Integer.parseInt(tirosLibres[1])
						: 0;
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
			statistic.setAssists(asistencias != null && !"".equals(asistencias) ? Integer.parseInt(asistencias) : 0);
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

}