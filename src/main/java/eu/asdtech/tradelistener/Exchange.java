package eu.asdtech.tradelistener;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Exchange {

	protected Set<TradeListener> listeners = ConcurrentHashMap.newKeySet();

	public void addListener(TradeListener listener) {
		listeners.add(listener);
	}

	public void removeListener(TradeListener listener) {
		listeners.remove(listener);
	}

	public abstract void run();
}
