package com.taxifleet.server;

import com.taxifleet.server.dao.DBObserver;
import com.taxifleet.server.network.NetworkNode;

public interface TaxiServer extends NetworkNode<TaxiServer>, DBObserver,
		ServerObservable, ServerObserver {

	// some client acessible methods
	public void checkIn();

	public void startTableWatchers();

	@Override
	public TaxiServer clone();
}
