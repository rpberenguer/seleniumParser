package es.fantasymanager.data.business;

import java.io.Serializable;

import lombok.Data;

@Data
public class TradeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String playerToAdd;
	private String playerToRemove;
}
