package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.TradeRequest;
import es.fantasymanager.services.TradeParserService;

@RestController
public class TradeParserController {

	@Autowired
	private TradeParserService service;

	@PostMapping(value = "/parser/trade")
	public String trade(@RequestBody TradeRequest request) throws MalformedURLException {

		service.doTrade(request.getPlayerToAdd(), request.getPlayerToRemove());

		return "Parseo OK";
	}
}
