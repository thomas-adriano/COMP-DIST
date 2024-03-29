package com.taxifleet.server.network.rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import com.taxifleet.server.BasicServer;
import com.taxifleet.server.TaxiServer;

public class TaxiServerNodes implements NoteElements<TaxiServer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -848383090796256443L;

	public static final String REMOTE_NAME = "NETWORK_NODES";

	private TaxiServer actualNode;
	private TaxiServer firstNode;
	private int size;
	private static TaxiServerNodes instance;

	private TaxiServerNodes() {

	}

	public static TaxiServerNodes getInstance() {
		if (instance == null)
			instance = new TaxiServerNodes();

		return instance;
	}

	public static void main(String[] args) throws UnknownHostException,
			RemoteException {
		TaxiServerNodes t = new TaxiServerNodes();
		InetAddress local = InetAddress.getLocalHost();
		BasicServer b1 = new BasicServer(local);
		BasicServer b2 = new BasicServer(local);
		BasicServer b3 = new BasicServer(local);
		BasicServer b4 = new BasicServer(local);
		BasicServer b5 = new BasicServer(local);

		t.addNode(b1);
		t.addNode(b2);
		t.addNode(b3);
		t.addNode(b4);
		t.addNode(b5);

		while (t.hasNext()) {
			System.out.println("count");
			t.next();
		}

	}

	@Override
	public boolean hasNext() throws RemoteException {
		return this.actualNode != null;
	}

	@Override
	public TaxiServer next() throws RemoteException {
		if (actualNode == null)
			throw new RuntimeException("NetworkNodes is empty.");

		TaxiServer result = actualNode.clone();

		actualNode = actualNode.getNextNode();

		return result;
	}

	@Override
	public void addNode(TaxiServer node) throws RemoteException {
		size++;
		if (this.actualNode == null) {
			this.actualNode = node;
			return;
		}

		TaxiServer actual = this.actualNode;

		while (actual.getNextNode() != null) {
			actual = actual.getNextNode();
		}

		actual.setNextNode(node);
	}

	@Override
	public int size() {
		return size;
	}


}
