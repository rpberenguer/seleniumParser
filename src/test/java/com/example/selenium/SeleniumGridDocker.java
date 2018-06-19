package com.example.selenium;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;

public class SeleniumGridDocker {
	
	private static final String URL_ESPN = "http://games.espn.com/fba/signin?redir=http://games.espn.go.com/fba/leagueoffice?leagueId=511966";
	
	WebDriver driver;
	
	@BeforeTest
	public void Driver() throws MalformedURLException {

		String Browser = "Firefox";

		if (Browser.equals("Firefox")) {
			DesiredCapabilities dcap = DesiredCapabilities.firefox();
			driver = new RemoteWebDriver(new URL("http://172.17.0.6:5555/wd/hub"), dcap);
		} else if (Browser.equals("Chrome")) {
			String exePath = "/Users/saraddhungel/Downloads/chromedriver";
			System.setProperty("webdriver.chrome.driver", exePath);
			DesiredCapabilities dcap = DesiredCapabilities.chrome();
			driver = new RemoteWebDriver(new URL("http://172.17.0.5:5555/wd/hub"), dcap);
		}
	}

	@Test
	public void doThese() {
		driver.get("http://www.google.com");
		WebElement hello = driver.findElement(By.xpath("//input[@id='lst-ib']"));
		hello.sendKeys("Java Code Geeks");
		hello.submit();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement hello1 = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("javacodegeeks")));

		WebDriver Driver = new Augmenter().augment(driver);
		File srcFile = ((TakesScreenshot) Driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File("image.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
