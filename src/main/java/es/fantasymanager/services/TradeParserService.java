package es.fantasymanager.services;

import java.io.IOException;
import java.util.List;

import es.fantasymanager.data.business.TradeData;

public interface TradeParserService {

	public void doTrade(List<TradeData> tradeList) throws IOException;
}
