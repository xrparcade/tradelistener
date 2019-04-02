package eu.asdtech.tradelistener;

import eu.asdtech.tradelistener.models.Trade;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradeListenerLoggingImpl implements TradeListener {

	@Override
	public void onNewTrade(Trade trade) {
		log.info("{}", trade);
	}

}
