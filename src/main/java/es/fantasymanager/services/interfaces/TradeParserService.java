package es.fantasymanager.services.interfaces;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface TradeParserService {

	public void doTrade(Map<String, String> tradeMap, LocalDateTime tradeDate) throws IOException;
}
