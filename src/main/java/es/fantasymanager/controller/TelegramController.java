package es.fantasymanager.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.rest.request.TelegramRequest;
import es.fantasymanager.services.interfaces.TelegramService;

@RestController
public class TelegramController {

	@Autowired
	private TelegramService service;

	@PostMapping(value = "/sendMessage")
	public String sendMessage(@RequestBody TelegramRequest request) throws IOException {

		return service.sendMessage(request.getText(), request.getChatId());
	}
}
