package com.parser.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class SeleniumGridDockerParser extends TestBase {

	private static final String URL_ESPN = "http://games.espn.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=511966";

//	private WebDriver driver;
//	private String playerToAdd;
//	private String playerToRemove;

//	@BeforeTest
//	public void Driver() throws MalformedURLException {
//
//		playerToAdd = "1404";
//		playerToRemove = "470";
//		String Browser = "Chrome";
//
//		if (Browser.equals("Firefox")) {
//			DesiredCapabilities dcap = DesiredCapabilities.firefox();
//			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dcap);
//		} else if (Browser.equals("Chrome")) {
//			DesiredCapabilities dcap = DesiredCapabilities.chrome();
//			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dcap);
//		}
//	}

	@Test
	public void doThese() {
		
		System.out.println("Test Started! " + Thread.currentThread().getId());
		
		// Get driver
		WebDriver driver = getDriver();
		
		driver.get(URL_ESPN);
		driver.switchTo().defaultContent(); // you are now outside both frames
		driver.switchTo().frame("disneyid-iframe");
		driver.getPageSource();

		// Print the title
		System.out.println("Title: " + driver.getTitle());

		// Login
		final WebElement email = driver.findElement(By.xpath("//input[@type='email']"));
		email.sendKeys("rpberenguer@gmail.com");

		final WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
		password.sendKeys("8ad3aah4");

		final WebElement signupButton = driver
				.findElement(By.xpath("//button[@class='btn btn-primary btn-submit ng-isolate-scope']"));
		signupButton.click();

		// Wait WebDriver
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 5000);

		// Wait until Tab is clickable
		WebElement playerTab = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#games-tabs li:nth-of-type(3) > a")));
		playerTab.click();

		// Find (+) Add Player Linkg & click()
		WebElement addPlayerLink = driver.findElement(By.cssSelector("#plyr" + getPlayerToAdd() + " td:nth-of-type(4) > a"));
		addPlayerLink.click();

		// Find checkBox to Remove Player
		WebElement removePlayerCheck = driver.findElement(By.cssSelector("#plyr" + getPlayerToRemove() + " td:nth-of-type(1) > input"));
		removePlayerCheck.click();

		// Find submitButton
		WebElement submitButton = driver.findElement(By.cssSelector("input[name='btnSubmit'][value='Submit Roster'] "));
		submitButton.click();

		// Find confirmButton
		WebElement confirmButton = driver.findElement(By.cssSelector("input[type='submit'][name='confirmBtn'] "));
		confirmButton.click();

		System.out.println("Test Ended! " + Thread.currentThread().getId());
	}
}
