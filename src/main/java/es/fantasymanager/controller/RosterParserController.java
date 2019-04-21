package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.interfaces.RosterParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RosterParserController {

	@Autowired
	private RosterParserService service;

	@PostMapping(value = "/parser/rosters")
	public String getRosters() throws MalformedURLException {

		log.info("Inicio parseo rosters");
		service.getRosters();
		log.info("Fin parseo rosters");

		return "Parseo Rosters OK";
	}
}
