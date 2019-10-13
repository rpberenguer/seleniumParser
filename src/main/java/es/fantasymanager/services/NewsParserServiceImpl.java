package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import es.fantasymanager.configuration.TelegramConfig;
import es.fantasymanager.data.entity.News;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.NewsRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.services.interfaces.NewsParserService;
import es.fantasymanager.utils.Constants;
import es.fantasymanager.utils.FantasyManagerHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsParserServiceImpl implements NewsParserService, Constants {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
			.appendPattern("MMM d, yyyy, h:mm a z")
			// .parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
			.toFormatter(Locale.getDefault());

	@Autowired
	private transient NewsRepository newsRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient FantasyManagerHelper fmHelper;

	@Autowired
	private TelegramConfig telegramConfig;

//	@Autowired
//	private SeleniumGridDockerHub hub;

	@Override
	@Transactional
	public void parseNews() throws MalformedURLException {

		log.info("Parse News Started! " + Thread.currentThread().getId());

//		final News news = new News();
//		news.setTitle("abc");
//
//		newsRepository.save(news);
//
//		throw new RuntimeException("not found...");

		// Driver
		// hub.setupDriver("chrome");
		// final WebDriver driver = hub.getDriver();
//		final WebDriver driver = new ChromeDriver();
		final WebDriver driver = new FirefoxDriver();
		final WebDriverWait wait = new WebDriverWait(driver, 90);

		try {
			driver.get(URL_ROTOWORLD_NEWS);

			// telegramService.sendImageFromUrl();

			final List<WebElement> tableNewsList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_ROTOWORLD_NEWS_LIST));

			for (final WebElement newsRow : tableNewsList) {
				parserNewsInfo(newsRow);
			}

			// String text = "<b>raul perez berenguer</b>\r\n" + " El mejor!!!\r\n";
			// String textWithEmoji = EmojiParser.parseToUnicode("emoji: :smile:" + text);
			// telegramService.sendMessage(textWithEmoji);

		} catch (final Exception e) {
			log.error("Error tratando news.", e);
		} finally {
			driver.close();
		}

	}

	private void parserNewsInfo(WebElement newsRow) throws Exception {
		final News news = new News();

		// Date
		try {
			final WebElement date = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_DATE);
			final LocalDateTime dateTime = LocalDateTime.parse(date.getText(), formatter);
			log.info("date {}, datTime {}", date.getText(), dateTime);

			final List<News> newsList = newsRepository.findByDateTime(dateTime);
			if (!newsList.isEmpty()) {
				log.info("News ya parseadas previamente con fecha {}", dateTime);
				return;
			}

			news.setDateTime(dateTime);
		} catch (final NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		// Player info
		Player player = null;
		try {
			final WebElement playerInfo = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_HEADER);

			final String href = playerInfo.getAttribute("href");
			final String playerName = StringUtils.substringAfterLast(href, "/");
			final String[] nameSplited = StringUtils.split(playerName, "-");

			final List<Player> players = playerRepository.findPlayerByNameContaining(nameSplited[0], nameSplited[1]);

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
		} catch (final NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
			return;
		}

		// log.info("href {}, player {}", href, playerName);

		// Title
		try {
			final WebElement title = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_TITLE);
			log.info("title {}", title.getText());
			news.setTitle(title.getText());
		} catch (final NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		try {
			// Summary
			final WebElement summary = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_SUMMARY);
			log.info("summary {}", summary.getText());
			news.setSummary(summary.getText());
		} catch (final NoSuchElementException ex) {
			log.warn("Elemento no encontrado: {}", ex.getMessage());
		}

		newsRepository.save(news);

		// Enviamos news por telegram
		final String text = "<b>" + player.getName() + "</b>\r\n" + news.getTitle() + "\r\n";
		fmHelper.sendMessageByStatistics(text, telegramConfig.getNewsChatId(), player);
	}
}
