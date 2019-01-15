package es.fantasymanager.data.jms;

import lombok.Data;

@Data
public class TradeJmsMessageData {

	private String playerToAdd;
	private String playerToRemove;
}
