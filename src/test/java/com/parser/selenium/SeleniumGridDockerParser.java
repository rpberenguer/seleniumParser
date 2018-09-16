package com.parser.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.common.base.Function;

import es.fantasymanager.utils.Constants;

public class SeleniumGridDockerParser extends TestBase implements Constants {

	@Test
	@Parameters(value={"playerToAdd", "playerToRemove"})
	public void doThese(String playerToAdd, String playerToRemove) {

		System.out.println("Test Started! " + Thread.currentThread().getId());

		// Get driver
		WebDriver driver = getDriver();

		driver.get(URL_ESPN);
		driver.switchTo().defaultContent(); // you are now outside both frames
		driver.switchTo().frame(LOGIN_IFRAME);
		driver.getPageSource();

		// Print the title
		System.out.println("Title: " + driver.getTitle());

		// Login
		final WebElement email = driver.findElement(BY_EMAIL_INPUT);
		email.sendKeys("rpberenguer@gmail.com");

		final WebElement password = driver.findElement(BY_PASSWORD_INPUT);
		password.sendKeys("8ad3aah4");

		final WebElement signupButton = driver
				.findElement(BY_SUBMIT_LOGIN_BUTTON);
		signupButton.click();

		// Wait WebDriver
		driver.switchTo().defaultContent();
//		WebDriverWait wait = new WebDriverWait(driver, 90);
		
//		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)       
//				.withTimeout(20, TimeUnit.SECONDS)    
//				.pollingEvery(5, TimeUnit.SECONDS)    
//				.ignoring(NoSuchElementException.class); 
//		
//		WebElement aboutMe= wait.until(new Function<WebDriver, WebElement>() {       
//			public WebElement apply(WebDriver driver) { 
//			return driver.findElement(BY_PLAYERS_TAB);     
//			 }  
//			});
//		
//		aboutMe.click();

		// Wait until Tab is clickable
//		WebElement playerTab = wait.until(ExpectedConditions.elementToBeClickable(BY_PLAYERS_TAB));
//		WebElement playerTab = driver.findElement(BY_PLAYERS_TAB);
//		playerTab.click();
		
		driver.get(URL_PLAYERS);

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

		System.out.println("Test Ended! " + Thread.currentThread().getId());
	}
}
