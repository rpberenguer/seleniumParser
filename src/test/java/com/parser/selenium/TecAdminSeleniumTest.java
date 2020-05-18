package com.parser.selenium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TecAdminSeleniumTest implements Constants {

	public static void main(String[] args) throws IOException, InterruptedException {

		final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

		final DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		final FirefoxOptions chromeOptions = new FirefoxOptions();
		chromeOptions.addArguments("--no-sandbox");
		// chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("--disable-extensions");
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		driver.set(new RemoteWebDriver(new URL("http://192.168.1.123:4444/wd/hub"), capabilities));

		final WebDriver webDriver = driver.get();
		// webDriver.navigate().to("http://www.google.com");
		// System.out.println("Google1 Test's Page title is: " + webDriver.getTitle() +"
		// " + "Thread Id: " + Thread.currentThread().getId());

		final String urlRosters = "http://fantasy.espn.com/basketball/league/rosters?leagueId=97189";
		final WebDriverWait wait = new WebDriverWait(webDriver, 90);

		//		getCookies(webDriver);

		// login
		login(webDriver, wait, urlRosters);

		//		createCookies(webDriver);

		// Team Links
		final List<WebElement> fantasyTeamList = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_FANTASY_TEAM_DIV));

		for (final WebElement fantasyTeamElement : fantasyTeamList) {

			// Buscamos titulo del equipo fantasy
			final WebElement fantasyTeamSpan = fantasyTeamElement.findElement(BY_FANTASY_TEAM_TITLE);
			log.debug("Fantasy Team {}", fantasyTeamSpan.getText());
		}

		webDriver.quit();
	}

	private static void getCookies(WebDriver webDriver) {
		try {

			final File file = new File("Cookies.data");
			final FileReader fileReader = new FileReader(file);
			final BufferedReader Buffreader = new BufferedReader(fileReader);
			String strline;
			while ((strline = Buffreader.readLine()) != null) {
				final StringTokenizer token = new StringTokenizer(strline, ";");
				while (token.hasMoreTokens()) {
					final String name = token.nextToken();
					final String value = token.nextToken();
					final String domain = token.nextToken();
					final String path = token.nextToken();
					Date expiry = null;

					String val;
					if (!(val = token.nextToken()).equals("null")) {
						expiry = new Date(val);
					}
					final Boolean isSecure = new Boolean(token.nextToken()).booleanValue();
					final Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					System.out.println(ck);
					webDriver.manage().addCookie(ck); // This will add the stored cookie to your current session
				}
			}

			Buffreader.close();

		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void createCookies(WebDriver webDriver) {
		// create file named Cookies to store Login Information
		final File file = new File("Cookies.data");
		try {
			// Delete old file if exists
			file.delete();
			file.createNewFile();
			final FileWriter fileWrite = new FileWriter(file);
			final BufferedWriter Bwrite = new BufferedWriter(fileWrite);
			// loop for getting the cookie information

			// loop for getting the cookie information
			for (final Cookie ck : webDriver.manage().getCookies()) {
				Bwrite.write(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ ck.getExpiry() + ";" + ck.isSecure());
				Bwrite.newLine();
			}
			Bwrite.close();
			fileWrite.close();

		} catch (final Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void login(final WebDriver driver, final WebDriverWait wait, final String url) {

		// Get URL
		driver.get(url);

		// Wait for frame
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("disneyid-iframe"));
		driver.switchTo().defaultContent(); // you are now outside both frames

		driver.switchTo().frame("disneyid-iframe");
		driver.getPageSource();

		// Print the title
		log.info("Title: " + driver.getTitle());

		// Login
		final WebElement email = wait.until(ExpectedConditions.elementToBeClickable(BY_EMAIL_INPUT));
		email.click();
		email.sendKeys("rpberenguer@gmail.com");

		final WebElement password = wait.until(ExpectedConditions.elementToBeClickable(BY_PASSWORD_INPUT));
		password.click();
		password.sendKeys("8ad3aah4espn");

		final WebElement signupButton = wait.until(ExpectedConditions.elementToBeClickable(BY_SUBMIT_LOGIN_BUTTON));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", signupButton);

		log.info("Login ok!");

		driver.switchTo().defaultContent();
	}
}
