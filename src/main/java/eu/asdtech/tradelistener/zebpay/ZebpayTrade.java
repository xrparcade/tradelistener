package eu.asdtech.tradelistener.zebpay;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ZebpayTrade {

	@JsonProperty("trans_id")
	private long id;

	@JsonProperty("fill_qty")
	private BigDecimal fillQty;

	@JsonProperty("fill_price")
	private BigDecimal fillPrice;

	@JsonProperty("fill_flags")
	private int fillFlags;

	private String currencyPair;

	private long lastModifiedDate;
}
