package es.fantasymanager.services;

import java.io.IOException;
import java.time.Instant;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeParserServiceImpl implements TradeParserService, Constants {

//	@Autowired
//	private SeleniumGridDockerHub hub;

	@Autowired
	private transient TelegramService telegramService;

//	private Object lock1 = new Object();

	@Override
	public void doTrade(String playerToAdd, String playerToRemove) throws IOException {

		log.info("Trade Started! " + Thread.currentThread().getId());

		// Get driver
//		hub.setupDriver("chrome");
//		WebDriver driver = hub.getDriver();

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// Print the webdriver
		log.info("Webdriver: " + driver.toString());
		WebDriverWait wait = new WebDriverWait(driver, 90);

		// Timing
		long startTimeInSec = Instant.now().getEpochSecond();

		try {
//			synchronized (lock1) {
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
//			}

			driver.switchTo().defaultContent();
//			WebDriverWait wait = new WebDriverWait(driver, 90);

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
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", removePlayerLink);

			// Find continueButton
			WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONTINUE_TRADE_BUTTON));
			log.info("Continue button finded.");
//			continueButton.click();
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);

			// Find confirmButton
			WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONFIRM_TRADE_BUTTON));
			log.info("Confirm button finded.");
//			confirmButton.click();
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);

			// Obtenemos tiempo
			Long endTimeInSec = Instant.now().getEpochSecond();

			String text = String.format("Trade de %s por %s, ok!. Tiempo %s segs.", playerToAdd, playerToRemove,
					endTimeInSec - startTimeInSec);

			// Logeamos + telegram
			log.info(text);
			telegramService.sendMessage(text);

		} catch (Exception e) {

			String text = String.format("Trade de %s por %s, ko.", playerToAdd, playerToRemove);

			// Logeamos + telegram
			log.error(text, e);
			telegramService.sendMessage(text);
		} finally {
			// Close
//			driver.close();
		}
	}
}
