package es.fantasymanager.data.rest.request;

import lombok.Data;

@Data
public class TradeRequest {

	private String playerToAdd;

	private String playerToRemove;
}
