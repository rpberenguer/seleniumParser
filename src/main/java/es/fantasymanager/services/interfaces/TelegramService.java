package es.fantasymanager.services.interfaces;

import java.io.IOException;

public interface TelegramService {

	public String sendMessage(String text) throws IOException;

	public String sendMessage(String text, String emoji) throws IOException;

	public String sendImageFromUrl();

}
