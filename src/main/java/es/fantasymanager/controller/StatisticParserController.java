package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.StatisticRequest;
import es.fantasymanager.services.interfaces.StatisticParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class StatisticParserController {

	@Autowired
	private StatisticParserService service;

	@PostMapping(value = "/parser/statistics")
	public String getStatistics(@RequestBody StatisticRequest request) throws MalformedURLException {

		log.info("Inicio parseo staistics");
		service.getStatistics(request.getStartDate(), request.getEndDate());
		log.info("Fin parseo statistics");

		return "Parseo Statistics OK";
	}
}
