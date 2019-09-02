package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.Assert;

import es.fantasymanager.configuration.YAMLConfig;
import es.fantasymanager.data.repository.ParameterRepository;
import es.fantasymanager.services.interfaces.TransactionParserService;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.FantasyManagerHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionParserServiceImpl implements TransactionParserService, Constants {

	@Autowired
	private transient ParameterRepository parameterRepository;

	@Autowired
	private YAMLConfig myConfig;

	@Override
	@Transactional
	public void getLastTransactions() throws MalformedURLException {

		log.info("Transaction Parser Started! " + Thread.currentThread().getId());

		// Driver
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);

		// Login
		FantasyManagerHelper.login(driver, wait, URL_RECENT_ACTIVITY + myConfig.getLeagueId());

		try {
			// parametro de ultima transaccion
			String strLastTransaction = parameterRepository.findByCode(LAST_TRANSACTION_DATE).getValue();
			LocalDateTime lastTransactionDate = LocalDateTime.parse(strLastTransaction, formatterTransaction);

			String formatDateTime = lastTransactionDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			log.debug("Formatted Time: " + formatDateTime);

			// Seleccionamos fecha de transacciones en el combo
			final WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.cssSelector(String.format(TRANSACTIONS_START_DATE_OPTIONS, formatDateTime))));

			log.debug("Option Date: " + webElement.getAttribute("value"));
			webElement.click();

			// Buscamos paginas
			final List<WebElement> paginationNavList = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_PAGINATION_NAV_LIST));

			if (!CollectionUtils.isEmpty(paginationNavList)) {

				for (int i = paginationNavList.size(); i >= 1; i--) {

					final WebElement paginationNavElement = wait.until(ExpectedConditions
							.elementToBeClickable(By.cssSelector(String.format(BY_PAGINATION_NAV_LIST_ELEMENT, i))));

					log.debug("pagination list: " + paginationNavElement);
					jsExecutor.executeScript("arguments[0].click();", paginationNavElement);
//					driver.getPageSource();

					waitForLoad(driver);

					getTransactionsByPage(driver);
				}
			}

		} catch (Exception e) {
			log.error("Error tratando transactions.", e);
		} finally {
			// Quit driver
//			driver.quit();
		}

		log.info("Transaction Parser Ended! " + Thread.currentThread().getId());
	}

	private void getTransactionsByPage(WebDriver driver) {
		log.debug("-------- getTransactionsByPage ---------");

		WebDriverWait wait = new WebDriverWait(driver, 5);

//		try {
//			Thread.sleep(1000);

		// Buscamos transacciones
		final List<WebElement> transactionList = wait.until(ExpectedConditions
				.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_TRANSACTION_SPAN_LIST)));

		Collections.reverse(transactionList);
		String action = null;
		for (WebElement webElement : transactionList) {
			String spanClass = webElement.getAttribute("class");
			action = StringUtils.substringBetween(spanClass, "waiver-", " db");
			doAction(webElement, action);
		}

//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(2000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	private void doAction(WebElement webElement, String action) {
		WebElement fantasyTeamElement = webElement.findElement(BY_TRANSACTION_FANTASY_TEAM_ELEMENT);
		String fantasyTeamName = fantasyTeamElement.getAttribute("title");

		WebElement playerElement = webElement.findElement(BY_TRANSACTION_PLAYER_ELEMENT);
		String playerName = playerElement.getText();

		log.debug("Team {}, action {}, player {}", fantasyTeamName, playerName, action);

	}
}
