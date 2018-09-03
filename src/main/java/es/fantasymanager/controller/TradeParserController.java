package es.fantasymanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.TradeRequest;
import es.fantasymanager.services.TradeParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TradeParserController {

	@Autowired
	private TradeParserService service;


	@GetMapping("/hello")
	public String test2() {
		return "Hola FantasyManager!!!!!!!!!!";
	}

	@PostMapping(value = "/parser/trade")
	public String trade(@RequestBody TradeRequest request) {

		log.info("Inicio parseo estadisticas");
		service.doTrade(request.getPlayerToAdd(), request.getPlayerToRemove());
		log.info("Fin parseo estadisticas");

		return "Parseo OK";
	}
}
