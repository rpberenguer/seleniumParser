package es.fantasymanager.data.business;

import java.util.Date;

import lombok.Data;

@Data
public class TransactionData {

	private String fantasyTeamName;

	private String playerNameAdded;

	private String playerNameDropped;

	private Date date;

}
