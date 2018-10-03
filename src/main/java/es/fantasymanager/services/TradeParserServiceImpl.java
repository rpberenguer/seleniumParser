package es.fantasymanager.services;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.SeleniumGridDockerHub;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TradeParserServiceImpl implements TradeParserService, Constants {

	@Autowired
	private SeleniumGridDockerHub hub;

	@Override
	public void doTrade(String playerToAdd, String playerToRemove) throws MalformedURLException {

		log.info("Trade Started! " + Thread.currentThread().getId());

		
		// Get driver
		hub.setupDriver("chrome");
		WebDriver driver = hub.getDriver();
		
		try {
			driver.get(URL_ADD_PLAYERS);
			driver.switchTo().defaultContent(); // you are now outside both frames
			driver.switchTo().frame(LOGIN_IFRAME);
			driver.getPageSource();

			// Print the title
			log.info("Title: " + driver.getTitle());

			// Login
			final WebElement email = driver.findElement(BY_EMAIL_INPUT);
			email.sendKeys("rpberenguer@gmail.com");

			final WebElement password = driver.findElement(BY_PASSWORD_INPUT);
			password.sendKeys("8ad3aah4");

			final WebElement signupButton = driver.findElement(BY_SUBMIT_LOGIN_BUTTON);
			signupButton.click();

			// Wait WebDriver
			driver.switchTo().defaultContent();
			WebDriverWait wait = new WebDriverWait(driver, 90);

			// Find (+) Add Player Linkg & click()
			WebElement addPlayerLink = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath(String.format(ADD_PLAYER_LINK, playerToAdd))));
			addPlayerLink.click();

			// Find checkBox to Remove Player
			WebElement removePlayerLink = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(String.format(REMOVE_PLAYER_LINK, playerToRemove))));
			// js executor
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", removePlayerLink);

			// Find submitButton
			WebElement continueButton = driver.findElement(BY_CONTINUE_TRADE_BUTTON);
			continueButton.click();

			// Find confirmButton
			WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONFIRM_TRADE_BUTTON));
			confirmButton.click();
		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Trade Ended! " + Thread.currentThread().getId());
	}
}
