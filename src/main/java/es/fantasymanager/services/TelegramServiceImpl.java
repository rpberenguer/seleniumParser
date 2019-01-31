package es.fantasymanager.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;

@Service
public class TelegramServiceImpl implements TelegramService {

	@Value("${telegram.token}")
	private String token;

	@Value("@${telegram.chatId}")
	private String chatId;

	@Override
	public String sendMessage(String text) throws IOException {

		TelegramBot bot = new TelegramBot(token);
		// Create your bot passing the token received from @BotFather
		SendMessage request = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
		SendResponse response = bot.execute(request);
		return response.description();

//		String urlString = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=@" + chatId + "&text=%s";
//
//		urlString = String.format(urlString, text);
//
//		URL url = new URL(urlString);
//		URLConnection conn = url.openConnection();
//
//		InputStream is = new BufferedInputStream(conn.getInputStream());
//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//		String inputLine = "";
//		StringBuilder sb = new StringBuilder();
//		while ((inputLine = br.readLine()) != null) {
//			sb.append(inputLine);
//		}
//		String response = sb.toString();
//		return response;
	}

	@Override
	public String sendImageFromUrl() {
		TelegramBot bot = new TelegramBot(token);
		SendPhoto request = new SendPhoto(chatId,
				"http://a.espncdn.com/combiner/i?img=/i/headshots/NBA/players/full/3206.png&w=96&h=70");

		SendResponse response = bot.execute(request);
		return response.description();

	}
}
