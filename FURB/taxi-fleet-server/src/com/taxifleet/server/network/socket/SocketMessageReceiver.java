package com.taxifleet.server.network.socket;

public interface SocketMessageReceiver<T> {

	public T receive();
	
}
