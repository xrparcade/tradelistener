package eu.asdtech.tradelistener;

import eu.asdtech.tradelistener.models.Trade;

public interface TradeListener {
	public void onNewTrade(Trade trade);
}
