package com.taxifleet.server.dao;

public interface TableObservable {

	public void registerObserver(TableObserver o);
	
	public void notifyObservers();
	
}
