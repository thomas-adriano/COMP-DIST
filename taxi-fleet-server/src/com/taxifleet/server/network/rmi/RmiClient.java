package com.taxifleet.server.network.rmi;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.Context;

public class RmiClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6509554338787491275L;
	private static final Logger LOGGER = LogManager.getLogger(RmiClient.class);
	private Context context;
	private static Registry registry;

	public static void main(String[] args) throws UnknownHostException {

		InetAddress addr = InetAddress.getLocalHost();
		RmiClient cliente = new RmiClient();
		RemoteObjectRetriever retriever = cliente.startClient(addr);
		
		TaxiServerNodes nodes = retriever.rerieve(TaxiServerNodes.REMOTE_NAME);
	}

	public RemoteObjectRetriever startClient(InetAddress serverAddress) {
		LOGGER.info("Starting RMI client...");
		RemoteObjectRetriever retriever = null;
		try {
			context = Context.getContext();
			int port = context.getDefaultRmiPort();

			registry = LocateRegistry.getRegistry(
					serverAddress.getHostAddress(), port);
			retriever = new RemoteObjectRetriever();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("RMI client started.");
		return retriever;
	}

	public class RemoteObjectRetriever implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4607324362617065981L;

		private RemoteObjectRetriever() {

		}

		@SuppressWarnings("unchecked")
		public <T extends Remote> T rerieve(String remoteObjectName) {
			T remoteObject = null;
			try {
				remoteObject = (T) registry.lookup(remoteObjectName);
				LOGGER.trace("Remote object retrieved.");
			} catch (NotBoundException | RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return remoteObject;
		}
	}

}
