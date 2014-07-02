package com.taxifleet.server.dao;

public interface DBObservable {

	public void notifyObservers();
	public void registerObserver(DBObserver o);
}
