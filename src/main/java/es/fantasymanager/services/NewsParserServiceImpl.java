package es.fantasymanager.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import es.fantasymanager.data.entity.News;
import es.fantasymanager.data.entity.Player;
import es.fantasymanager.data.repository.NewsRepository;
import es.fantasymanager.data.repository.PlayerRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsParserServiceImpl implements NewsParserService, Constants {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd MMM. yyyy H:mm")
//			.parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
			.toFormatter(Locale.getDefault());

	@Autowired
	private transient NewsRepository newsRepository;

	@Autowired
	private transient PlayerRepository playerRepository;

	@Autowired
	private transient TelegramService telegramService;

	@Override
	@Transactional
	public void parseNews() {

		log.info("Parse News Started! " + Thread.currentThread().getId());

//		Locale.setDefault(Locale.ENGLISH);

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);

		try {
			driver.get(URL_ROTOWORLD_NEWS);

//			telegramService.sendImageFromUrl();

			final List<WebElement> tableNewsList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_ROTOWORLD_NEWS_LIST));

			for (WebElement newsRow : tableNewsList) {
				parserNewsInfo(newsRow);
			}

		} catch (IOException e) {
			log.error("Error enviando mensaje telegram.", e);
		} finally {
			driver.close();
		}

	}

	private void parserNewsInfo(WebElement newsRow) throws IOException {
		News news = new News();

		// Date
		WebElement date = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_DATE);
		LocalDateTime dateTime = LocalDateTime.parse(date.getText(), formatter);
		log.info("date {}, datTime {}", date.getText(), dateTime);

		List<News> newsList = newsRepository.findByDateTime(dateTime);
		if (!newsList.isEmpty()) {
			log.info("News ya parseadas previamente con fecha {}", dateTime);
			return;
		}

		news.setDateTime(dateTime);

		// Player info
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

		Player player = players.get(0);
		news.setPlayer(player);

//		log.info("href {}, player {}", href, playerName);

		// Report
		WebElement title = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_TITLE);
		log.info("title {}", title.getText());
		news.setTitle(title.getText());

		// Impact
		WebElement summary = newsRow.findElement(BY_ROTOWORLD_NEWS_ARTICLE_SUMMARY);
		log.info("summary {}", summary.getText());
		news.setSummary(summary.getText());

		newsRepository.save(news);

		String text = "<b>" + player.getName() + "</b>\r\n" + news.getTitle() + "\r\n";
		telegramService.sendMessage(text);

	}
}
