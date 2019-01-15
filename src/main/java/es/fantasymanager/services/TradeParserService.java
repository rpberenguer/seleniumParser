package es.fantasymanager.services;

import java.io.IOException;

public interface TradeParserService {

	public void doTrade(String playerToAdd, String playerToRemove) throws IOException;
}
