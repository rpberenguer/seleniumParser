package es.fantasymanager.data.business;

import java.io.Serializable;

import lombok.Data;

@Data
public class TradeData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1068021065628158280L;

	private String playerToAdd;
	private String playerToRemove;
}
