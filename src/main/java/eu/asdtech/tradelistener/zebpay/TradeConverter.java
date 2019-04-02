package eu.asdtech.tradelistener.zebpay;

import eu.asdtech.tradelistener.models.Trade;

public class TradeConverter {

	public Trade convert(ZebpayTrade source) {
		Trade out = new eu.asdtech.tradelistener.models.Trade();

		out.setInstrument(source.getCurrencyPair());
		out.setAmount(source.getFillQty());
		out.setPrice(source.getFillPrice());

		return out;
	}
}
