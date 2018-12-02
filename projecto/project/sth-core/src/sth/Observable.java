package sth;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
import sth.Observer;


abstract class Observable implements Serializable {

	/** Serial number for serialization. */
  	private static final long serialVersionUID = 201811272014L;
	private Set<Observer> _observers = new HashSet<Observer>();

	void addObserver(Observer obs) {
		_observers.add(obs);
	}

	void removeObserver(Observer obs) {
		_observers.remove(obs);
	}

	void notifyObservers(String notification) {
		for (Observer obs : _observers) {
			obs.receiveNotification(notification);
		}
	}
}