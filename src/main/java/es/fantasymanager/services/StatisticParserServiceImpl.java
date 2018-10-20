package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.entity.Game;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Team;
import es.fantasymanager.data.repository.GameRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticParserServiceImpl implements StatisticParserService, Constants {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Autowired
	private transient GameRepository gameRespository;
	
	private WebDriver driver;

	@Override
	@Transactional
	public void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws MalformedURLException {

		log.info("Statistic Parser Started! " + Thread.currentThread().getId());

		try {
			driver.get(URL_SCHEDULE + dateTimeFrom.format(formatter));

			// Print the title
			log.info("Title: " + driver.getTitle());

			// Tables with all week games
			final List<WebElement> weekGames = driver.findElements(BY_SCHEDULE_TABLE_DAY);

			for (WebElement dayGames : weekGames) {

				List<WebElement> gameLinks = dayGames.findElements(BY_GAME_LINK);
				for (WebElement gameLink : gameLinks) {					
					log.debug("Game link {}", gameLink);
					
					final String gameId = StringUtils.substringAfter(gameLink.getAttribute("href"), GAME_LINK);
					Game game = new Game();
					game.setNbaId(gameId);
					
					gameRespository.save(game);
				}
				
				// Sumamos un día a la fecha inicial
				dateTimeFrom = dateTimeFrom.plusDays(1);

				// Si hemos superado la fecha fin hemos llegado al final del
				// parseo
				if (dateTimeFrom.isAfter(dateTimeTo)) {
					log.info("Fin del parseo!!");
					return;
				}
			}
		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Statistic Parser Ended! " + Thread.currentThread().getId());
	}
	
	@PostConstruct
	private void postConstruct() {
		driver = new ChromeDriver();
	}
}
