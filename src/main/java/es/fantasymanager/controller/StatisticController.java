package es.fantasymanager.controller;

import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.business.StatisticAvgDto;
import es.fantasymanager.data.rest.request.StatisticRequest;
import es.fantasymanager.services.StatisticService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class StatisticController {

	@Autowired
	private StatisticService service;

	@PostMapping(value = "/statistics")
	public List<StatisticAvgDto> getStatisticsAvg(@RequestBody StatisticRequest request) throws MalformedURLException {

		log.info("Inicio get media estadisticas");
		List<StatisticAvgDto> statisticsAvg = service.getStatisticsAvg(request.getStartDate(), request.getEndDate());
		log.info("Fin get media estadisticas");

		return statisticsAvg;
	}
}
