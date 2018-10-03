package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.SeleniumGridDockerHub;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RosterParserServiceImpl implements RosterParserService, Constants {

	@Autowired
	private SeleniumGridDockerHub hub;

	@Override
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
			
			for (WebElement webElement : teamLinks) {
				log.debug("Team link {}", webElement.getAttribute("href"));
			}


		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Roster Parser Ended! " + Thread.currentThread().getId());
	}
}
