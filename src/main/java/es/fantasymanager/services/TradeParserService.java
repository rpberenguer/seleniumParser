package es.fantasymanager.services;

import java.net.MalformedURLException;

public interface TradeParserService {

	public void doTrade(String playerToAdd, String playerToRemove) throws MalformedURLException;
}
