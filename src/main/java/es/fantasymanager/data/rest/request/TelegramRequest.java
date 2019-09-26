package es.fantasymanager.data.rest.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class TelegramRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String text;

	private String chatId;
}
