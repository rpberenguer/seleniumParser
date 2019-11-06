package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.configuration.SeleniumConfig;
import es.fantasymanager.data.entity.FantasyTeam;
import es.fantasymanager.data.entity.Parameter;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.FantasyTeamRepository;
import es.fantasymanager.data.repository.ParameterRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.services.interfaces.FantasyTeamParserService;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.FantasyManagerHelper;
import es.fantasymanager.utils.SeleniumGridDockerHub;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FantasyTeamParserServiceImpl implements FantasyTeamParserService, Constants {

	@Autowired
	private SeleniumGridDockerHub hub;
	
	@Autowired
	private transient FantasyTeamRepository fantasyTeamRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient ParameterRepository parameterRepository;

	@Autowired
	private SeleniumConfig myConfig;

	@Override
	@Transactional
	public void getFantasyTeams() throws MalformedURLException {

		log.info("Fantasy Team Parser Started! " + Thread.currentThread().getId());

		// Driver
//		final WebDriver driver = new ChromeDriver();
		hub.setupDriver("chrome");
		final WebDriver driver = hub.getDriver();
		final WebDriverWait wait = new WebDriverWait(driver, 90);

		// Login
		FantasyManagerHelper.login(driver, wait, myConfig.getUrlLeagueRosters());

		try {
			// Team Links
			final List<WebElement> fantasyTeamList = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_FANTASY_TEAM_DIV));

			for (final WebElement fantasyTeamElement : fantasyTeamList) {

				// Buscamos titulo del equipo fantasy
				final WebElement fantasyTeamSpan = fantasyTeamElement.findElement(BY_FANTASY_TEAM_TITLE);
				final FantasyTeam fantasyTeam = fantasyTeamRepository.findByTeamName(fantasyTeamSpan.getText());
				if (fantasyTeam == null) {
					log.error("Fantasy Team no encontrado {}", fantasyTeamSpan.getText());
					continue;
				}

				log.debug("Fantasy Team {}", fantasyTeam.toString());

				// Buscamos jugadores del equipo fantasy
				final List<WebElement> playerList = fantasyTeamElement.findElements(BY_FANTASY_TEAM_PLAYER_IMG);

				for (final WebElement playerElement : playerList) {

					final String src = playerElement.getAttribute("src");
					final String nbaId = StringUtils.substringBetween(src, PLAYER_IMG_PREFIX, ".png");
					log.debug("Player nbaId {}", nbaId);

					// Buscamos jugador y lo asociamos al equipo fantasy
					final Player player = playerRepository.findPlayerByNbaId(nbaId);

					if (player == null) {
						log.error("Player no encontrado {}", nbaId);
						continue;
					}

					player.setFantasyTeam(fantasyTeam);
					playerRepository.save(player);
				}
			}

			// guardamos el param lastTransaction con la fecha actual
			LocalDateTime lastTransactionDate = LocalDateTime.now();
			Parameter parameter = parameterRepository.findByCode(LAST_TRANSACTION_DATE);
			
			if(parameter == null)
			{
				parameter = new Parameter();
				parameter.setCode(LAST_TRANSACTION_DATE);
			}
			parameter.setValue(lastTransactionDate.format(formatterTransaction));
			parameterRepository.save(parameter);

		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Fantasy Team Parser Ended! " + Thread.currentThread().getId());
	}
}
