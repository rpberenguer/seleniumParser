package es.fantasymanager.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.business.TradeData;
import es.fantasymanager.data.rest.request.TradeRequest;
import es.fantasymanager.services.interfaces.TradeParserService;

@RestController
public class TradeParserController {

	@Autowired
	private TradeParserService service;

	@PostMapping(value = "/parser/trade")
	public String trade(@RequestBody TradeRequest tradeRequest) throws IOException {

		Map<String, String> tradeMap = new HashMap<String, String>();

		for (TradeData tradeData : tradeRequest.getTradeList()) {
			tradeMap.put(tradeData.getPlayerToAdd(), tradeData.getPlayerToRemove());
		}

		service.doTrade(tradeMap, tradeRequest.getTradeDate());

		return "Parseo OK";
	}
}
