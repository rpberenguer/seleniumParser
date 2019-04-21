package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.interfaces.TransactionParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TransactionParserController {

	@Autowired
	private TransactionParserService service;

	@PostMapping(value = "/parser/transactions")
	public String getLastTransactions() throws MalformedURLException {

		log.info("Inicio parseo staistics");
		service.getLastTransactions();
		log.info("Fin parseo statistics");

		return "Parseo Transacciones OK";
	}

}
