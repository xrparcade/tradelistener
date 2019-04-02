package eu.asdtech.tradelistener.exceptions;

public class GenericExchangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6407677194119511364L;

	public GenericExchangeException(Throwable e) {
		super(e);
	}

	public GenericExchangeException(String msg) {
		super(msg);
	}

	public GenericExchangeException() {
		super();
	}
}
