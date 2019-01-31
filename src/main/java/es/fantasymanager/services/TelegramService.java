package es.fantasymanager.services;

import java.io.IOException;

public interface TelegramService {

	public String sendMessage(String text) throws IOException;

	public String sendImageFromUrl();
}
