package eu.asdtech.tradelistener;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import eu.asdtech.tradelistener.exceptions.ConfigException;
import eu.asdtech.tradelistener.exceptions.GenericExchangeException;
import eu.asdtech.tradelistener.exceptions.UnsupportedExchangeException;
import eu.asdtech.tradelistener.zebpay.ZebpayExchange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

	public static void main(String[] args)
			throws ConfigException, UnsupportedExchangeException, GenericExchangeException {

		// configure log4j
		BasicConfigurator.configure();

		Config config = readConfig();

		Exchange exchange = selectExchange(config);

		if (config.isSound()) {
			exchange.addListener(new TradeListenerLoggingAndSoundImpl());
		} else {
			exchange.addListener(new TradeListenerLoggingImpl());
		}

		exchange.run();
	}

	private static Config readConfig() throws ConfigException {
		InputStream configStream = App.class.getClass().getResourceAsStream("/config.yml");
		if (configStream == null) {
			throw new ConfigException("No config file config.yml found in path");
		}

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			Config config = mapper.readValue(configStream, Config.class);
			config.validate();
			return config;
		} catch (JsonParseException | JsonMappingException e) {
			throw new ConfigException("Error parsing config file");
		} catch (IOException e) {
			throw new ConfigException("I/O error while reading config file");
		}
	}

	private static Exchange selectExchange(Config config)
			throws UnsupportedExchangeException, GenericExchangeException {
		switch (config.getSource()) {
		case zebpay:
			return new ZebpayExchange(config.getZebpay());
		default:
			log.error("Unsupported exchange {}", config.getSource());
			throw new UnsupportedExchangeException();
		}
	}
}
