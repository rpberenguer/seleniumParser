package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

	@Override
	@Transactional
	public void getLastTransactions() throws MalformedURLException {

		log.info("Transaction Parser Started! " + Thread.currentThread().getId());

		String strLastTransaction = parameterRepository.findByCode(LAST_TRANSACTION_DATE).getValue();
		LocalDateTime lastTransactionDate = LocalDateTime.parse(strLastTransaction, formatterTransaction);

		String formatDateTime = lastTransactionDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		log.debug("Formatted Time: " + formatDateTime);

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);
		WebDriverWait wait2 = new WebDriverWait(driver, 5);
		JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);

		// Login
		FantasyManagerHelper.login(driver, wait, URL_RECENT_ACTIVITY);

		try {
			// Seleccionamos fecha de transacciones en el combo
			final WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.cssSelector(String.format(TRANSACTIONS_START_DATE_OPTIONS, formatDateTime))));

			log.debug("Option Date: " + webElement.getAttribute("value"));
			webElement.click();

			// Buscamos paginas
			final List<WebElement> paginationNavList = wait2
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_PAGINATION_NAV_LIST));

			if (!CollectionUtils.isEmpty(paginationNavList)) {

				for (int i = paginationNavList.size(); i >= 1; i--) {

					final WebElement paginationNavElement = wait.until(ExpectedConditions
							.elementToBeClickable(By.cssSelector(String.format(BY_PAGINATION_NAV_LIST_ELEMENT, i))));
					log.debug("pagination list: " + paginationNavElement);
//					paginationNavElement.click();
					jsExecutor.executeScript("arguments[0].click();", paginationNavElement);

					getTransactionsByPage(wait2);
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

	private void getTransactionsByPage(WebDriverWait wait2) {
		log.debug("getTransactionsByPage");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
