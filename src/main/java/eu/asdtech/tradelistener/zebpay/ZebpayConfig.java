package eu.asdtech.tradelistener.zebpay;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ZebpayConfig {
	private long interval;
	private String market;

	public void validate() {
		if (interval < 1 || interval > 300) {
			log.warn("Zebpay interval is outside suggested values (1 to 300)");
			this.interval = 1;
		}

		if (market == null || market.isEmpty()) {
			log.warn("Market is not configured for Zebpay. Using default XRP-EUR");
			this.market = "XRP-EUR";
		}
	}
}
