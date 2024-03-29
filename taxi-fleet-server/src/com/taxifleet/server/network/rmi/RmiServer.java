package com.taxifleet.server.network.rmi;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.Context;

public class RmiServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4389713457904840706L;
	private static final Logger LOGGER = LogManager.getLogger(RmiServer.class);
	private Context context;
	private static RemoteObjectBinder binder;
	private static Registry registry;

	public static void main(String[] args) throws RemoteException {
		LOGGER.info("Starting RMI Server...");
		RmiServer server = new RmiServer();
		RemoteObjectBinder binder = server.startServer();

		TaxiServerNodes servers = TaxiServerNodes.getInstance();
		binder.bindRemoteObject(TaxiServerNodes.REMOTE_NAME, servers);

	}

	public RemoteObjectBinder startServer() {
		RemoteObjectBinder binder = null;
		try {
			context = Context.getContext();
			int rmiPort = context.getDefaultRmiPort();

			registry = LocateRegistry.getRegistry(rmiPort);

			binder = new RemoteObjectBinder();

			LOGGER.info("RMI server ready.");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return binder;
	}

	/**
	 * Respons�vel por vincular Remote Objects ao Registry
	 * 
	 * @author Thomas
	 *
	 */
	public class RemoteObjectBinder implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4938193933460846403L;

		// previne a cria��o standalone deste objeto, ele deve ser instanci�vel
		// somente ap�s a chamada de RmiServer.startServer()
		private RemoteObjectBinder() {

		}

		public void bindRemoteObject(String remoteObjectName, Remote remote) {
			try {

				Remote stub = (Remote) UnicastRemoteObject
						.exportObject(remote, 0);

				registry.bind(remoteObjectName, stub);
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
