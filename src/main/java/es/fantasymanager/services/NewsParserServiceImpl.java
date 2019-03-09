package es.fantasymanager.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.vdurmont.emoji.EmojiParser;

import es.fantasymanager.data.entity.News;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.NewsRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsParserServiceImpl implements NewsParserService, Constants {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("d MMM. yyyy H:mm")
//			.parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
			.toFormatter(Locale.getDefault());

	@Autowired
	private transient NewsRepository newsRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient TelegramService telegramService;

	@Autowired
	private transient StatisticService statisticService;

	@Override
	@Transactional
	public void parseNews() {

		log.info("Parse News Started! " + Thread.currentThread().getId());

//		Locale.setDefault(Locale.ENGLISH);

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
//		System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
//		System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
		WebDriver driver = new ChromeDriver();
//		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);

		try {
			driver.get(URL_ROTOWORLD_NEWS);

//			telegramService.sendImageFromUrl();

			final List<WebElement> tableNewsList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_ROTOWORLD_NEWS_LIST));

			for (WebElement newsRow : tableNewsList) {
				parserNewsInfo(newsRow);
			}

//			String text = "<b>raul perez berenguer</b>\r\n" + " El mejor!!!\r\n";
//			String textWithEmoji = EmojiParser.parseToUnicode("emoji: :smile:" + text);
//			telegramService.sendMessage(textWithEmoji);

		} catch (IOException e) {
			log.error("Error enviando mensaje telegram.", e);
		} finally {
			driver.close();
		}

	}

	private void parserNewsInfo(WebElement newsRow) throws IOException {
		News news = new News();

		// Date
		try {
			WebElement date = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_DATE);
			LocalDateTime dateTime = LocalDateTime.parse(date.getText(), formatter);
			log.info("date {}, datTime {}", date.getText(), dateTime);

			List<News> newsList = newsRepository.findByDateTime(dateTime);
			if (!newsList.isEmpty()) {
				log.info("News ya parseadas previamente con fecha {}", dateTime);
				return;
			}

			news.setDateTime(dateTime);
		} catch (NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		// Player info
		Player player = null;
		try {
			WebElement playerInfo = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_HEADER);

			String href = playerInfo.getAttribute("href");
			String playerName = StringUtils.substringAfterLast(href, "/");
			String[] nameSplited = StringUtils.split(playerName, "-");

			List<Player> players = playerRepository.findPlayerByNameContaining(nameSplited[0], nameSplited[1]);

			if (CollectionUtils.isEmpty(players)) {
				log.warn("Jugador no encontrado {}", playerName);
				return;
			}
			if (players.size() > 1) {
				log.error("Más de un jugador encontrado para el nombre {}", playerName);
				return;
			}

			player = players.get(0);

			news.setPlayer(player);
		} catch (NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
			return;
		}

//		log.info("href {}, player {}", href, playerName);

		// Title
		try {
			WebElement title = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_TITLE);
			log.info("title {}", title.getText());
			news.setTitle(title.getText());
		} catch (NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		try {
			// Summary
			WebElement summary = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_SUMMARY);
			log.info("summary {}", summary.getText());
			news.setSummary(summary.getText());
		} catch (NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		newsRepository.save(news);

		String text = "<b>" + player.getName() + "</b>\r\n" + news.getTitle() + "\r\n";
		String textWithEmoji = EmojiParser.parseToUnicode(":warning:" + text);
		telegramService.sendMessage(textWithEmoji);

	}
}
