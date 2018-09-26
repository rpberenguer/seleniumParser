package com.parser.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import es.fantasymanager.utils.Constants;

public class SeleniumGridDockerParser extends TestBase implements Constants {

	@Test
	@Parameters(value={"playerToAdd", "playerToRemove"})
	public void doThese(String playerToAdd, String playerToRemove) {

		System.out.println("Test Started! " + Thread.currentThread().getId());

		// Get driver
		WebDriver driver = getDriver();

		driver.get(URL_ADD_PLAYERS);
		driver.switchTo().defaultContent(); // you are now outside both frames
		driver.switchTo().frame(LOGIN_IFRAME);
		driver.getPageSource();

		// Print the title
		System.out.println("Title: " + driver.getTitle());

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
		WebDriverWait wait = new WebDriverWait(driver, 90);
		
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

		System.out.println("111111111111");
		// Wait until Tab is clickable
		WebElement addPlayerLink = wait.until(ExpectedConditions.elementToBeClickable(BY_ADD_PLAYER_LINK));
		addPlayerLink.click();
		
		System.out.println("2222222222222");
		
		// Find checkBox to Remove Player
		WebElement removePlayerLink = wait.until(ExpectedConditions.elementToBeClickable(BY_REMOVE_PLAYER_LINK));
		
		// js executor
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", removePlayerLink);
		
//		Actions actions = new Actions(driver);
//		actions.moveToElement(removePlayerLink).click().perform();
//		
//		removePlayerLink.click();
		
		System.out.println("3333333333333");

		// Find submitButton
		WebElement continueButton = driver.findElement(BY_CONTINUE_TRADE_BUTTON);
		continueButton.click();
		
		System.out.println("44444444444444");
		
		// Find submitButton
//		WebElement submitButton = driver.findElement(BY_SUBMIT_TRADE_BUTTON);
//		submitButton.click();

		// Find confirmButton
		WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(BY_CONFIRM_TRADE_BUTTON));
		confirmButton.click();

		System.out.println("Test Ended! " + Thread.currentThread().getId());
	}
}
