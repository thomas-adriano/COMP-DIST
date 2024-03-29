package com.taxifleet.server.network;

import java.io.Serializable;

import com.taxifleet.server.NodeElement;

public interface NetworkNode<T> extends Serializable, NodeElement<T> {

	public void shout();

	public void startListeningTimers(long time);

}
