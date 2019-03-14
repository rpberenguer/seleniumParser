package es.fantasymanager.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FantasyManagerHelper implements Constants {

	public static void login(final WebDriver driver, final WebDriverWait wait, final String url) {
		driver.get(url);

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
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", signupButton);

		log.info("Login ok!");

		driver.switchTo().defaultContent();
	}
}
