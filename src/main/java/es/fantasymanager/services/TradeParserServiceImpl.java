package es.fantasymanager.services;

import org.openqa.selenium.By;
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
	public void doTrade(String playerToAdd, String playerToRemove) {

		log.debug("Trade Started! " + Thread.currentThread().getId());

		// Get driver
		WebDriver driver = hub.getDriver();

		driver.get(URL_ESPN);
		driver.switchTo().defaultContent(); // you are now outside both frames
		driver.switchTo().frame(LOGIN_IFRAME);
		driver.getPageSource();

		// Print the title
		log.debug("Title: " + driver.getTitle());

		// Login
		final WebElement email = driver.findElement(BY_EMAIL_INPUT);
		email.sendKeys("rpberenguer@gmail.com");

		final WebElement password = driver.findElement(BY_PASSWORD_INPUT);
		password.sendKeys("*****");

		final WebElement signupButton = driver
				.findElement(BY_SUBMIT_LOGIN_BUTTON);
		signupButton.click();

		// Wait WebDriver
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 5000);

		// Wait until Tab is clickable
		WebElement playerTab = wait.until(ExpectedConditions.elementToBeClickable(BY_PLAYERS_TAB));
		playerTab.click();

		// Find (+) Add Player Linkg & click()
		WebElement addPlayerLink = driver.findElement(By.cssSelector("#plyr" + playerToAdd + " td:nth-of-type(4) > a"));
		addPlayerLink.click();

		// Find checkBox to Remove Player
		WebElement removePlayerCheck = driver.findElement(By.cssSelector("#plyr" + playerToRemove + " td:nth-of-type(1) > input"));
		removePlayerCheck.click();

		// Find submitButton
		WebElement submitButton = driver.findElement(BY_SUBMIT_TRADE_BUTTON);
		submitButton.click();

		// Find confirmButton
		WebElement confirmButton = driver.findElement(BY_CONFIRM_TRADE_BUTTON);
		confirmButton.click();

		// Quit driver
		driver.quit();

		log.debug("Trade Ended! " + Thread.currentThread().getId());
	}
}
