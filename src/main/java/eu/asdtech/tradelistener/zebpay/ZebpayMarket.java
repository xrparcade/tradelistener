package eu.asdtech.tradelistener.zebpay;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ZebpayMarket {

	private BigDecimal buy;
	private BigDecimal sell;
	private BigDecimal market;
	private BigDecimal volume;
	private BigDecimal pricechange;
	private String pair;
	private String virtualCurrency;
	private String currency;

	@JsonProperty("24hoursHigh")
	private BigDecimal dailyHigh;

	@JsonProperty("24hoursLow")
	private BigDecimal dailyLow;
}
