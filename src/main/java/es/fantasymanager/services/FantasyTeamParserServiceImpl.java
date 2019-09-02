package es.fantasymanager.services;

import java.net.MalformedURLException;
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

import es.fantasymanager.data.entity.FantasyTeam;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.FantasyTeamRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.services.interfaces.FantasyTeamParserService;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.FantasyManagerHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FantasyTeamParserServiceImpl implements FantasyTeamParserService, Constants {

	@Autowired
	private transient FantasyTeamRepository fantasyTeamRespository;

	@Autowired
	private transient PlayerRepository playerRespository;

	@Override
	@Transactional
	public void getFantasyTeams() throws MalformedURLException {

		log.info("Fantasy Team Parser Started! " + Thread.currentThread().getId());

		// Driver
		final WebDriver driver = new ChromeDriver();
		final WebDriverWait wait = new WebDriverWait(driver, 90);

		// Login
		FantasyManagerHelper.login(driver, wait, URL_LEGAUE_ROSTERS);

		try {
			// Team Links
			final List<WebElement> fantasyTeamList = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_FANTASY_TEAM_DIV));

			for (final WebElement fantasyTeamElement : fantasyTeamList) {

				// Buscamos titulo del equipo fantasy
				final WebElement fantasyTeamSpan = fantasyTeamElement.findElement(BY_FANTASY_TEAM_TITLE);
				final FantasyTeam fantasyTeam = fantasyTeamRespository.findByTeamName(fantasyTeamSpan.getText());
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
					final Player player = playerRespository.findPlayerByNbaId(nbaId);

					if (player == null) {
						log.error("Player no encontrado {}", nbaId);
						continue;
					}

					player.setFantasyTeam(fantasyTeam);
					playerRespository.save(player);
				}
			}

		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Fantasy Team Parser Ended! " + Thread.currentThread().getId());
	}
}
