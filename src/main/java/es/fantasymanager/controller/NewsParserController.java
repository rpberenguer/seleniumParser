package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.interfaces.NewsParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NewsParserController {

	@Autowired
	private NewsParserService service;

	@PostMapping(value = "/parser/news")
	public String parseNews() throws MalformedURLException {

		log.info("Inicio parseo news");
		service.parseNews();

//		ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
//		// or Executors.newScheduledThreadPool(2); if you have multiple tasks
//		LocalDateTime todayAt1Am = LocalDate.now().atTime(0, 17);
//		exe.schedule(() -> System.out.println("Executed at:" + (new Date())),
//				LocalDateTime.now().until(todayAt1Am, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);

		log.info("Fin parseo news");

		return "Parseo news OK";
	}
}
