package es.fantasymanager.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeParserServiceImpl implements TradeParserService, Constants {

	@Autowired
	private transient TelegramService telegramService;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Override
	public void doTrade(Map<String, String> tradeMap, LocalDateTime tradeDate) throws IOException {

		log.info("Trade Started! " + Thread.currentThread().getId());

		// Get driver
//		hub.setupDriver("chrome");
//		WebDriver driver = hub.getDriver();

		// driver + wait + jsExecutor
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);
		JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);

		// Preparamos scheduledExecutor
		ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

		// Login
		login(driver, wait);

		try {

			// Store the current window handle
			String firstTab = driver.getWindowHandle();

			// Preparamos los trades con las ventanas de confirmacion
			int i = 1;
			for (Entry<String, String> entry : tradeMap.entrySet()) {

				// logica de fichaje
				prepareTrade(wait, jsExecutor, entry.getKey(), entry.getValue());

				if (i < tradeMap.entrySet().size()) {
					jsExecutor.executeScript("window.open()");
					List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
					driver.switchTo().window(tabs.get(i));
					driver.get(URL_ADD_PLAYERS);

					i++;
				}
			}

			// Volvemos a la primera pestaña
			driver.switchTo().window(firstTab);

			// Programamos el comiteo de los trades
			scheduledExecutor.schedule(() -> {
				try {
					commitTrade(wait, jsExecutor);

					List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
					for (int j = 1; j < tabs.size(); j++) {
						// Switch de tab
						driver.switchTo().window(tabs.get(j));
						// Esperamos 1 seg
						Thread.sleep(1000);

//					driver.switchTo().defaultContent();
						commitTrade(wait, jsExecutor);
					}
				} catch (InterruptedException e) {
					log.error("Error en Trade", e);
				} catch (IOException e) {
					log.error("Error enviando Telegram", e);
				}
			}, LocalDateTime.now().until(tradeDate, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);

		} finally {
			// Close
//			driver.quit();
		}
	}

	private void commitTrade(WebDriverWait wait, JavascriptExecutor jsExecutor) throws IOException {
		try {
			// Find confirmButton
			WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONFIRM_TRADE_BUTTON));
			log.info("Confirm button finded.");
			jsExecutor.executeScript("arguments[0].click();", confirmButton);
			
		} catch (Exception e) {

			String text = "Commit de Trade, ko.";

			// Logeamos + telegram
			log.error(text, e);
			telegramService.sendMessage(text);
		}
	}

	private void prepareTrade(WebDriverWait wait, JavascriptExecutor jsExecutor, String playerToAdd,
			String playerToRemove) throws IOException {

		try {
			// Buscamos player en bdd
			Player player = playerRepository.findPlayerByNbaId(playerToAdd);

			// Hacemos busqueda en el input text
			WebElement inputSearch = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[placeholder='Player Name']")));
			inputSearch.sendKeys(player.getName());

			String strPlayerSearchId = String.format(SEARCH_PLAYER_TEXT, playerToAdd);
			WebElement playerSearchMatches = wait
					.until(ExpectedConditions.elementToBeClickable(By.cssSelector(strPlayerSearchId)));
			playerSearchMatches.click();

			// Find (+) Add Player Linkg & click()
			WebElement addPlayerLink = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath(String.format(ADD_PLAYER_LINK, playerToAdd))));

			log.info("Player to add finded.");
			addPlayerLink.click();

			// Find checkBox to Remove Player
			WebElement removePlayerLink = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(String.format(REMOVE_PLAYER_LINK, playerToRemove))));

			log.info("Player to remove finded.");
			// js executor
			jsExecutor.executeScript("arguments[0].click();", removePlayerLink);

			// Find continueButton
			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONTINUE_TRADE_BUTTON));
			log.info("Continue button finded.");
//			continueButton.click();
			jsExecutor.executeScript("arguments[0].click();", continueButton);

//			String text = String.format("Trade de %s por %s, ok!", playerToAdd, playerToRemove);

			// Logeamos + telegram
//			log.info(text);
//			telegramService.sendMessage(text);
		} catch (Exception e) {

			String text = String.format("Trade de %s por %s, ko.", playerToAdd, playerToRemove);

			// Logeamos + telegram
			log.error(text, e);
			telegramService.sendMessage(text);
		}
	}

	private void login(WebDriver driver, WebDriverWait wait) {
		driver.get(URL_ADD_PLAYERS);

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
//				signupButton.click();
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", signupButton);

		log.info("Login ok!");

		driver.switchTo().defaultContent();
	}
}
