package com.taxifleet.server;

import java.rmi.Remote;

public interface Taxi extends Remote {

	public CarPlate getCarPlate();
	
}
