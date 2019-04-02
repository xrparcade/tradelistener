package eu.asdtech.tradelistener.models;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Trade {
	private String instrument;
	private BigDecimal amount;
	private BigDecimal price;
}
