package es.fantasymanager.services;

import java.util.List;

import javax.transaction.Transactional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fantasymanager.data.repository.NewsRepository;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsParserServiceImpl implements NewsParserService, Constants {

	@Autowired
	private transient NewsRepository newsRepository;

	@Override
	@Transactional
	public void parseNews() {

		log.info("Parse News Started! " + Thread.currentThread().getId());

		// Driver
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 90);

		try {
			driver.get(URL_ROTOWORLD_NEWS);

			final List<WebElement> tableNewsList = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(BY_ROTOWORL_NEWS_DIV));

			for (WebElement newsRow : tableNewsList) {
				parserNewsInfo(newsRow);
			}

		} finally {
			driver.close();
		}

	}

	private void parserNewsInfo(WebElement newsRow) {
		WebElement playerInfo = newsRow.findElement(By.cssSelector("div.headline div.player a"));
		String href = playerInfo.getAttribute("href");
		log.info("href {}", href);

	}
}
