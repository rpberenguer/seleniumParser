package es.fantasymanager.jms;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import es.fantasymanager.data.entity.Game;
import es.fantasymanager.data.jms.GameJmsMessageData;
import es.fantasymanager.data.jms.StatisticJmsMessageData;
import es.fantasymanager.data.repository.GameRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GameListener implements Constants {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Autowired
	private transient GameRepository gameRepository;

	@Autowired
	private JmsTemplate jmsTemplate;

	@JmsListener(destination = GAME_QUEUE, containerFactory = "myFactory")
	public void receiveMessage(@Payload GameJmsMessageData gameMessage, @Headers MessageHeaders headers,
			Message message, Session session) throws JMSException {

		log.info("Received <---" + gameMessage + "--->");

//		throw new NoSuchElementException("not found...");

		// Driver
		final WebDriver driver = new ChromeDriver();
		final WebDriverWait wait = new WebDriverWait(driver, 90);

		// Fechas a parsear
		LocalDate startDate = gameMessage.getStartDate();
		final LocalDate endDate = gameMessage.getEndDate();

		// Lista donde guardar los gameIds
		final List<String> gameIds = new ArrayList<>();

		// Timing
		final long startTimeInSec = Instant.now().getEpochSecond();

		try {
			driver.get(URL_SCHEDULE + startDate.format(formatter));

			final List<WebElement> tableDayList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_SCHEDULE_TABLE_DAY));
			for (final WebElement tableDay : tableDayList) {

				final List<WebElement> gameByDays = tableDay.findElements(BY_GAME_LINK);
				for (final WebElement gameLink : gameByDays) {
					final String href = gameLink.getAttribute("href");
					log.info("Game link {}", href);

					final String nbaId = StringUtils.substringAfter(href, GAME_LINK);

					Game game = gameRepository.findByNbaId(nbaId);
					if (game != null) {
						log.info("Partido ya insertado en BDD: {}", nbaId);
						break;
					} else {
						game = new Game();
					}

					gameIds.add(nbaId);
					game.setNbaId(nbaId);

					final Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
					game.setDate(date);

					gameRepository.save(game);
				}

				// Sumamos un día a la fecha inicial
				startDate = startDate.plusDays(1);

				// Si hemos superado la fecha fin hemos llegado al final del
				// parseo
				if (startDate.isAfter(endDate)) {
					break;
				}
			}
		} finally {
			// Cerramos driver
			driver.close();
		}

		final Long endTimeInSec = Instant.now().getEpochSecond();
		log.info("Partidos obtenidos entre las fechas {} y {}. Tiempo {}", gameMessage.getStartDate(),
				gameMessage.getEndDate(), endTimeInSec - startTimeInSec);

		// Enviamos mensaje cola de estadisticas
		final StatisticJmsMessageData statisticMessage = new StatisticJmsMessageData();
		statisticMessage.setGameIds(gameIds);

		log.info("sending with convertAndSend() to queue <" + statisticMessage + ">");
		jmsTemplate.convertAndSend(STATISTIC_QUEUE, statisticMessage);

	}

}