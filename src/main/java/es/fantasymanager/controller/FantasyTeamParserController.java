package es.fantasymanager.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.services.FantasyTeamParserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FantasyTeamParserController {

	@Autowired
	private FantasyTeamParserService service;

	@PostMapping(value = "/parser/fantasyTeams")
	public String getFantasyTeams() throws MalformedURLException {

		log.info("Inicio parseo equipos fantasy");
		service.getFantasyTeams();

		log.info("Fin parseo equipos fantasy");

		return "Parseo equipos fantasy OK";
	}
}
