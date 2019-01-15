package es.fantasymanager.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramServiceImpl implements TelegramService {

	@Value("${telegram.token}")
	private String token;

	@Value("${telegram.chatId}")
	private String chatId;

	public String sendMessage(String text) throws IOException {

		String urlString = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=@" + chatId + "&text=%s";

		urlString = String.format(urlString, text);

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		InputStream is = new BufferedInputStream(conn.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String inputLine = "";
		StringBuilder sb = new StringBuilder();
		while ((inputLine = br.readLine()) != null) {
			sb.append(inputLine);
		}
		String response = sb.toString();
		return response;
	}
}
