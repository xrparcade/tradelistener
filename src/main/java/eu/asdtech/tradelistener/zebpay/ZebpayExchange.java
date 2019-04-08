package eu.asdtech.tradelistener.zebpay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import eu.asdtech.tradelistener.Exchange;
import eu.asdtech.tradelistener.TradeListener;
import eu.asdtech.tradelistener.exceptions.GenericExchangeException;
import eu.asdtech.tradelistener.models.Trade;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZebpayExchange extends Exchange {

	private final String URI_ROOT = "https://www.zebapi.com/pro/v1/";
	private final ZebpayConfig config;
	private final OkHttpClient client = new OkHttpClient();
	private final ObjectMapper mapper;
	private final TradeConverter converter = new TradeConverter();

	public ZebpayExchange(final ZebpayConfig config) throws GenericExchangeException {
		this.mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.config = config;
		verifyMarket();
	}

	@Override
	public void run() {

		long processAfterDate = 0;

		while (!Thread.interrupted()) {
			List<ZebpayTrade> trades = getTrades();
			long max = 0;
			for (ZebpayTrade trade : trades) {
				if (trade.getLastModifiedDate() > max) {
					max = trade.getLastModifiedDate();
				}

				if (trade.getLastModifiedDate() > processAfterDate) {
					Trade out = converter.convert(trade);
					// warn: listener may block
					for (TradeListener listener : this.listeners) {
						listener.onNewTrade(out);
					}
				}
			}
			if (max > processAfterDate) {
				processAfterDate = max;
			}

			try {
				Thread.sleep(config.getInterval() * 1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void verifyMarket() throws GenericExchangeException {
		Request request = new Request.Builder().url(URI_ROOT + "/market").build();
		Response response;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			log.error("Could not connect to Zebpay");
			throw new GenericExchangeException(e);
		}

		if (response.code() != 200) {
			log.error("Unknown error from Zebpay. Http status {}", response.code());
			throw new GenericExchangeException();
		}

		List<ZebpayMarket> markets;
		try {
			markets = mapper.readValue(response.body().string(), new TypeReference<List<ZebpayMarket>>() {
			});
		} catch (IOException e) {
			log.error("Could not parse markets response from Zebpay");
			throw new GenericExchangeException(e);
		}

		for (ZebpayMarket m : markets) {
			if (config.getMarket().equals(m.getPair())) {
				return;
			}
		}

		throw new UnsupportedMarketException();
	}

	private List<ZebpayTrade> getTrades() {
		List<ZebpayTrade> trades = new ArrayList<>();

		Request request = new Request.Builder().url(URI_ROOT + "/market/" + config.getMarket() + "/trades").build();
		Response response;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			log.error("Could not connect to Zebpay");
			return trades;
		}

		if (response.code() == 429) {
			log.warn("No data fetched from Zebpay due to rate limits having been exceeded");
			return trades;
		} else if (response.code() != 200) {
			log.warn("No data fetched from Zebpay. Unknown error. Http response {}", response.code());
			return trades;
		}

		try {
			trades = mapper.readValue(response.body().string(), new TypeReference<List<ZebpayTrade>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Could not parse trades response from Zebpay");
		}

		return trades;

	}
}
