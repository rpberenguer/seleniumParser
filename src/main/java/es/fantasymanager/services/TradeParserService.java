package es.fantasymanager.services;

import java.io.IOException;
import java.util.Map;

public interface TradeParserService {

	public void doTrade(Map<String, String> tradeMap) throws IOException;
}
