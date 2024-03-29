package com.taxifleet.server.network.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.taxifleet.server.NodeElement;

public interface NoteElements<T extends NodeElement<?>> extends Remote, Serializable {
	
	public boolean hasNext() throws RemoteException;

	public T next() throws RemoteException;

	public void addNode(T node) throws RemoteException;
	
	public int size() throws RemoteException;
}
