package es.fantasymanager.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.business.StatisticAvgDto;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Season;
import es.fantasymanager.data.repository.SeasonRepository;
import es.fantasymanager.services.interfaces.StatisticService;
import es.fantasymanager.services.interfaces.TelegramService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FantasyManagerHelper implements Constants {

	@Autowired
	private transient SeasonRepository seasonRepository;

	@Autowired
	private transient TelegramService telegramService;

	@Autowired
	private transient StatisticService statisticService;

	public static void login(final WebDriver driver, final WebDriverWait wait, final String url) {

		// Get URL
		driver.get(url);

		// Wait for frame
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(LOGIN_IFRAME));
		driver.switchTo().defaultContent(); // you are now outside both frames

		driver.switchTo().frame(LOGIN_IFRAME);
		driver.getPageSource();

		// Print the title
		log.info("Title: " + driver.getTitle());

		// Login
		final WebElement email = wait.until(ExpectedConditions.elementToBeClickable(BY_EMAIL_INPUT));
		email.click();
		email.sendKeys("rpberenguer@gmail.com");

		final WebElement password = wait.until(ExpectedConditions.elementToBeClickable(BY_PASSWORD_INPUT));
		password.click();
		password.sendKeys("ilovethisgame&&&");

		final WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(BY_SUBMIT_LOGIN_BUTTON));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", signupButton);

		log.info("Login ok!");

		driver.switchTo().defaultContent();
	}

	public void sendMessageByStatistics(String text, String chatId, Player player) throws IOException {

		// obtenemos la temporada actual
		final Season season = seasonRepository.findByIsCurrentSeason(true);

		// obtenemos estadisticas del jugador
		final List<StatisticAvgDto> statistics = statisticService
				.getStatisticsAvg(DateUtils.asLocalDate(season.getStartDate()), LocalDate.now(), player.getNbaId());

		// Si no tiene estadisticas o tiene una media inferior al limite, no enviamos
		// emoji
		if (statistics.isEmpty() || statistics.get(0).getFantasyPointsAvg() == null
				|| statistics.get(0).getFantasyPointsAvg() < FANTASYPOINTS_TO_SEND_WARNING) {
			telegramService.sendMessage(text, chatId);
		} else {
			telegramService.sendMessage(text, chatId, EMOJI_WARNING);
		}
	}
}
