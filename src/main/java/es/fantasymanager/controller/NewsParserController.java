package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.NewsParserService;
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
		log.info("Fin parseo news");

		return "Parseo Rosters OK";
	}
}
