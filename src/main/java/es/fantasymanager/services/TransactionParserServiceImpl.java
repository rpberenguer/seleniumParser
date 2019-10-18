package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import es.fantasymanager.configuration.SeleniumConfig;
import es.fantasymanager.configuration.TelegramConfig;
import es.fantasymanager.data.business.TransactionData;
import es.fantasymanager.data.entity.FantasyTeam;
import es.fantasymanager.data.entity.Parameter;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.entity.Transaction;
import es.fantasymanager.data.repository.FantasyTeamRepository;
import es.fantasymanager.data.repository.ParameterRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.data.repository.TransactionRepository;
import es.fantasymanager.services.interfaces.TelegramService;
import es.fantasymanager.services.interfaces.TransactionParserService;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.DateUtils;
import es.fantasymanager.utils.FantasyManagerHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionParserServiceImpl implements TransactionParserService, Constants {

	@Autowired
	private transient TelegramService telegramService;

	@Autowired
	private transient ParameterRepository parameterRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient FantasyTeamRepository fantasyTeamRepository;

	@Autowired
	private transient TransactionRepository transactionRepository;

//	@Autowired
//	private transient FantasyManagerHelper fmHelper;

	@Autowired
	private SeleniumConfig seleniumConfig;

	@Autowired
	private TelegramConfig telegramConfig;

	@Override
	@Transactional
	public void getLastTransactions() throws MalformedURLException {

		log.info("Transaction Parser Started! " + Thread.currentThread().getId());

		// Driver
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 5);
		JavascriptExecutor jsExecutor = ((JavascriptExecutor) driver);

		// Login
		FantasyManagerHelper.login(driver, wait, seleniumConfig.getUrlRecentAtivity());

		try {
			// parametro de ultima transaccion
			Parameter lastTransactionParameter = parameterRepository.findByCode(LAST_TRANSACTION_DATE);
			LocalDateTime lastTransactionDate = getLastTransactionDate(lastTransactionParameter);

			String formatDateTime = lastTransactionDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			log.debug("Formatted Time: " + formatDateTime);

			// Seleccionamos fecha de transacciones en el combo 'Start Date'
			final List<WebElement> webElements = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.cssSelector(TRANSACTIONS_START_DATE_OPTIONS)));

			// si la última transaccion es del dia actual cogemos el último dia del combo
			// 'Start Date'
			if (LocalDate.now().equals(lastTransactionDate.toLocalDate())) {
				WebElement webElement = webElements.get(webElements.size() - 1);
				log.debug("Option Date: " + webElement.getAttribute("value"));
				webElement.click();
			} else {
				for (WebElement webElement : webElements) {
					log.debug("Option Date: " + webElement.getAttribute("value"));
					if (formatDateTime.equals(webElement.getAttribute("value"))) {
						log.debug("Clickamos");
						webElement.click();
					}
				}
			}

			// Buscamos paginas
			final List<WebElement> paginationNavList = driver.findElements(BY_PAGINATION_NAV_LIST);

			// Si hay paginacion
			if (!CollectionUtils.isEmpty(paginationNavList)) {

				for (int i = paginationNavList.size(); i >= 1; i--) {

					final WebElement paginationNavElement = wait.until(ExpectedConditions
							.elementToBeClickable(By.cssSelector(String.format(BY_PAGINATION_NAV_LIST_ELEMENT, i))));

					log.debug("pagination list: " + paginationNavElement);
					jsExecutor.executeScript("arguments[0].click();", paginationNavElement);
//					driver.getPageSource();

					waitForLoad(driver);

					getTransactionsByPage(driver, wait, lastTransactionParameter);
				}
			}
			// si no hay paginacion
			else {
				getTransactionsByPage(driver, wait, lastTransactionParameter);
			}

		} catch (Exception e) {
			log.error("Error tratando transactions.", e);
		} finally {
			// Quit driver
			driver.quit();
		}

		log.info("Transaction Parser Ended! " + Thread.currentThread().getId());
	}

	private void getTransactionsByPage(WebDriver driver, WebDriverWait wait, Parameter lastTransactionParameter) {
		log.debug("-------- getTransactionsByPage ---------");

//		final List<WebElement> transactionList = driver.findElements(BY_TRANSACTION_CELL_DIV);
		final List<WebElement> transactionList = wait
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BY_TRANSACTION_CELL_DIV));

		// ordenamos descendente
		Collections.reverse(transactionList);

		LocalDateTime lastTransactionDate = getLastTransactionDate(lastTransactionParameter);

		for (WebElement webElement : transactionList) {

			// validar si la fecha de la transaccion es superior o igual a la que hemos
			// guardado como parametro
			WebElement divTransactionDate = webElement.findElement(BY_TRANSACTION_DATE_DIV);
			String date = divTransactionDate.findElement(BY_TRANSACTION_DATE_DATE_DIV).getText();
			String time = divTransactionDate.findElement(BY_TRANSACTION_DATE_TIME_DIV).getText();

			LocalDateTime transactionDate = LocalDateTime.parse(date + " " + time, formatterTransaction);

			if (transactionDate.isBefore(lastTransactionDate)) {
				log.debug("Transaccion anterior: {} {}", date, time);
			} else {
				log.debug("Transaccion: {} {}", date, time);

				// Creamos Transaction
				TransactionData transactionData = new TransactionData();
				transactionData.setDate(DateUtils.asDate(transactionDate));

				// doTransaction
				doTransaction(webElement, transactionData);

				// guardamos tx
				persistTransaction(transactionData);
//				transactionRepository.save(transaction);

				// actualizamos parametro
				lastTransactionParameter.setValue(transactionDate.format(formatterTransaction));
				parameterRepository.save(lastTransactionParameter);
			}

		}
	}

	private void doTransaction(WebElement webElement, TransactionData transactionData) {

		// buscamos el fantasyTeam
		WebElement fantasyTeamElement = webElement.findElement(BY_TRANSACTION_FANTASY_TEAM_ELEMENT);
		String fantasyTeamName = fantasyTeamElement.getAttribute("title");

		transactionData.setFantasyTeamName(fantasyTeamName);

//		String text = "<b>" + fantasyTeamName + "</b>\r\n";

		// los elementos span hijos nos marca si es un added/dropped, add o drop
		final List<WebElement> spanElements = webElement.findElements(BY_TRANSACTION_DETAILS_SPAN);

		for (WebElement spanElement : spanElements) {
			doAction(spanElement, transactionData);
		}

		// enviamos transaccion por telegram
//		try {
//			telegramService.sendMessage(text, telegramConfig.getTransactionsChatId());
//		} catch (IOException e) {
//			log.error("Error enviando telegram {}.", text);
//		}

	}

	private void doAction(WebElement spanElement, TransactionData transactionData) {

		String spanClass = spanElement.getAttribute("class");
		String action = StringUtils.substringBetween(spanClass, "waiver-", " db");

		WebElement playerElement = spanElement.findElement(BY_TRANSACTION_PLAYER_ELEMENT);
		String playerName = playerElement.getText();

//		String emoji = "";
		if (ACTION_ADD.equals(action)) {
			transactionData.setPlayerNameAdded(playerName);
//			emoji = EMOJI_ARROW_UP;
		} else if (ACTION_DROP.equals(action)) {
			transactionData.setPlayerNameDropped(playerName);
//			emoji = EMOJI_ARROW_DOWN;
		} else {
			log.error("Action {} no válida.", action);
			return;
		}

//		log.debug("FantasyTeam {}, action {}, player {}", transactionData.getFantasyTeam().getTeamName(), action,
//				playerName);
//
//		return EmojiParser.parseToUnicode(emoji + " " + action + " " + playerName + "\r\n");

	}

	private void persistTransaction(TransactionData transactionData) {

		String fantasyTeamName = transactionData.getFantasyTeamName();
		String playerNameAdded = transactionData.getPlayerNameAdded();
		String playerNameDropped = transactionData.getPlayerNameDropped();
		Date date = transactionData.getDate();

		// buscamos fantasyTeam
		FantasyTeam fantasyTeam = fantasyTeamRepository.findByTeamName(fantasyTeamName);
		if (fantasyTeam == null) {
			log.error("FantasyTeam con nombre {} no encontrado.", fantasyTeamName);
			return;
		}

		// buscamos player added
		Player playerAdded = null;
		if (playerNameAdded != null) {
			playerAdded = playerRepository.findPlayerByName(playerNameAdded);
			if (playerAdded == null) {
				log.error("Jugador con nombre {} no encontrado.", playerNameAdded);
				return;
			}
		}

		// buscamos player dropped
		Player playerDropped = null;
		if (playerNameDropped != null) {
			playerDropped = playerRepository.findPlayerByName(playerNameDropped);
			if (playerDropped == null) {
				log.error("Jugador con nombre {} no encontrado.", playerNameDropped);
				return;
			}
		}

		// verificamos que no existe la trasaccion
		List<Transaction> transactions = new ArrayList<Transaction>();

		if (playerAdded != null && playerDropped != null) {
			transactions = transactionRepository.findByDateAndFantasyTeamAndPlayerAddedAndPlayerDropped(date,
					fantasyTeam, playerAdded, playerDropped);
		} else if (playerAdded != null) {
			transactions = transactionRepository.findByDateAndFantasyTeamAndPlayerAdded(date, fantasyTeam, playerAdded);
		} else {
			transactions = transactionRepository.findByDateAndFantasyTeamAndPlayerDropped(date, fantasyTeam,
					playerDropped);
		}

		if (transactions.isEmpty()) {

			// guardamos info de los jugadores
			if (playerAdded != null) {
				playerAdded.setFantasyTeam(fantasyTeam);
				playerRepository.save(playerAdded);
			}
			if (playerDropped != null) {
				playerDropped.setFantasyTeam(null);
				playerRepository.save(playerDropped);
			}

			// guardamos info de las transacciones
			Transaction transaction = new Transaction();
			transaction.setDate(date);
			transaction.setFantasyTeam(fantasyTeam);
			transaction.setPlayerAdded(playerAdded);
			transaction.setPlayerDropped(playerDropped);
			transactionRepository.save(transaction);

		} else {
			log.debug("Transaccion ya realizada: {}", transactionData);
		}
	}

	private static LocalDateTime getLastTransactionDate(Parameter lastTransactionParameter) {
		String strLastTransaction = lastTransactionParameter.getValue();
		LocalDateTime lastTransactionDate = LocalDateTime.parse(strLastTransaction, formatterTransaction);

		return lastTransactionDate;
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
}
