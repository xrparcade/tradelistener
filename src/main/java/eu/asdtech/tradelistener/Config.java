package eu.asdtech.tradelistener;

import eu.asdtech.tradelistener.models.Source;
import eu.asdtech.tradelistener.zebpay.ZebpayConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
class Config {
	private Source source;
	private boolean sound;
	private ZebpayConfig zebpay;

	public void validate() {
		if (source == null) {
			log.error("Config file is missing value for source");
		} else if (Source.zebpay.equals(source)) {
			if (zebpay == null) {
				zebpay = new ZebpayConfig();
			}
			zebpay.validate();
		}
	}
}
