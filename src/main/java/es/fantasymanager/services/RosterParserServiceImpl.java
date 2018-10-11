package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Team;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.data.repository.TeamRepository;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.SeleniumGridDockerHub;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RosterParserServiceImpl implements RosterParserService, Constants {

	@Autowired
	private SeleniumGridDockerHub hub;

	@Autowired
	private transient TeamRepository teamRespository;

	@Autowired
	private transient PlayerRepository playerRespository;

	@Override
	@Transactional
	public void getRosters() throws MalformedURLException {

		log.info("Roster Parser Started! " + Thread.currentThread().getId());

		// Get driver
		hub.setupDriver("chrome");
		WebDriver driver = hub.getDriver();

		try {
			driver.get(URL_TEAMS);

			// Print the title
			log.info("Title: " + driver.getTitle());

			// Team Links
			final List<WebElement> teamLinks = driver.findElements(BY_TEAM_LINK);

			for (WebElement teamLink : teamLinks) {

				String hrefTeam = teamLink.getAttribute("href");
				log.debug("Team link {}", hrefTeam);

				String[] codes = StringUtils.substringAfter(hrefTeam, TEAM_LINK).split("/");
				Team team = new Team();
				team.setShortCode(codes[0]);
				team.setLongCode(codes[1]);
				String name = teamLink.findElement(By.xpath("./../../../a/h2")).getText();
				team.setName(name);

				teamRespository.save(team);
			}

			List<Team> teams = (List<Team>) teamRespository.findAll();
//				teamLink.click();
				
			for (Team team : teams) {
				// Get Roster URL
				driver.get(URL_ESPN + TEAM_LINK + team.getShortCode() + "/" + team.getLongCode());

				// Team Links
				final List<WebElement> playerLinks = driver.findElements(BY_PLAYER_LINK);

				for (WebElement playerLink : playerLinks) {
					String hrefPlayer = playerLink.getAttribute("href");
					String nbaId = StringUtils.substringAfter(hrefPlayer, PLAYER_LINK);

					Player player = new Player();
					player.setName(playerLink.getText());
					player.setNbaId(nbaId);
					player.setTeam(team);

					playerRespository.save(player);
				}
			}

		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Roster Parser Ended! " + Thread.currentThread().getId());
	}
}
