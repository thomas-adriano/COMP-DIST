package com.taxifleet.server;

public interface ServerObserver {

	public void registerObserver(ServerObserver obs);

	public void update(ServerObservable subject);

}
