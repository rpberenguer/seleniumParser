package es.fantasymanager.services.interfaces;

import java.io.IOException;

public interface TelegramService {

	public String sendMessage(String text, String chatId) throws IOException;

	public String sendMessage(String text, String chatId, String emoji) throws IOException;

	public String sendImageFromUrl(String chatId);

}
