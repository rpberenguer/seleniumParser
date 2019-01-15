package es.fantasymanager.jms;

import java.io.IOException;
import java.time.Instant;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import es.fantasymanager.data.jms.TradeJmsMessageData;
import es.fantasymanager.services.TelegramService;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TradeListener implements Constants {

	@Autowired
	private transient TelegramService telegramService;

	@JmsListener(destination = STATISTIC_QUEUE)
	public void receiveMessage(@Payload TradeJmsMessageData tradeMessage, @Headers MessageHeaders headers,
			Message message, Session session) throws JMSException, IOException {

		log.info("Received <---" + tradeMessage + "--->");

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
			WebElement addPlayerLink = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(String.format(ADD_PLAYER_LINK, tradeMessage.getPlayerToAdd()))));

			log.info("Player to add finded.");
			addPlayerLink.click();

			// Find checkBox to Remove Player
			WebElement removePlayerLink = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath(String.format(REMOVE_PLAYER_LINK, tradeMessage.getPlayerToRemove()))));

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

			String text = String.format("Trade de %s por %s, ok!. Tiempo %s segs.", tradeMessage.getPlayerToAdd(),
					tradeMessage.getPlayerToRemove(), endTimeInSec - startTimeInSec);

			// Logeamos + telegram
			log.info(text);
			telegramService.sendMessage(text);

		} catch (Exception e) {

			String text = String.format("Trade de %s por %s, ko.", tradeMessage.getPlayerToAdd(),
					tradeMessage.getPlayerToRemove());

			// Logeamos + telegram
			log.error(text, e);
			telegramService.sendMessage(text);
		} finally {
			// Close
//			driver.close();
		}
	}
}