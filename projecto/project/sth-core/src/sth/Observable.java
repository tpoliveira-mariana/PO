package sth;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import sth.Observer;


abstract class Observable implements Serializable {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 201811272014L;
	private HashMap<Integer, Observer> _observers = new HashMap<Integer, Observer>();

	void addObserver(Observer obs) {
		_observers.put(obs.getId(), obs);
	}

	void removeObserver(Observer obs) {
		_observers.remove(obs.getId());
	}

	void notifyObservers(String notification) {
		for (Observer obs : _observers.values()) {
			obs.receiveNotification(notification);
		}
	}
}