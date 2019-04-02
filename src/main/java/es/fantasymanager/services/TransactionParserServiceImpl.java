package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.repository.ParameterRepository;
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
		LocalDateTime lastTransaction = LocalDateTime.parse(strLastTransaction, formatterTransaction);

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);

		// Login
		FantasyManagerHelper.login(driver, wait, URL_RECENT_ACTIVITY);

		try {

		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Transaction Parser Ended! " + Thread.currentThread().getId());
	}
}
