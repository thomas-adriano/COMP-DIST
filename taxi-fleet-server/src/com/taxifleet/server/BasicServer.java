package com.taxifleet.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.DatabaseWatcher;
import com.taxifleet.server.dao.PrecosTableWatcher;
import com.taxifleet.server.dao.StatusTableWatcher;
import com.taxifleet.server.dao.TaxiTableWatcher;
import com.taxifleet.server.dao.ViagemTableWatcher;
import com.taxifleet.server.dao.tables.DbTable;
import com.taxifleet.server.network.NetworkRequest;
import com.taxifleet.server.network.rmi.NoteElements;
import com.taxifleet.server.network.rmi.TaxiServerNodes;
import com.taxifleet.server.network.rmi.RmiClient;
import com.taxifleet.server.network.rmi.RmiClient.RemoteObjectRetriever;
import com.taxifleet.server.network.rmi.RmiServer;
import com.taxifleet.server.network.rmi.RmiServer.RemoteObjectBinder;
import com.taxifleet.server.network.socket.MulticastSocketMessageSender;
import com.taxifleet.server.network.socket.ShoutRequestListener;
import com.taxifleet.server.network.socket.ShoutResponseListener;
import com.taxifleet.server.network.socket.SocketMessageSender;
import com.taxifleet.utils.TimerExecutor;

//TODO ver necessidade de listener estarem em threads. Socket.accept() e DatagramSocket.receive() 
//j� tem o comportamento que eu busco utilizando Threads
public class BasicServer implements TaxiServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8434299929425496749L;
	private RmiClient rmiClient;
	private RmiServer rmiServer;
	private RemoteObjectRetriever retriever;
	private RemoteObjectBinder binder;
	private TaxiServer nextNode;
	/**
	 * 
	 */
	private Context context;
	private InetAddress localHost;
	private static NoteElements<TaxiServer> networkServers;
	private static final Logger LOGGER = LogManager.getLogger(TaxiServer.class);

	private DatabaseWatcher dbWatcher;
	private List<DbTable> updatedTables;

	public List<ServerObserver> serverObservers;

	public static void main(String[] args) {
		BasicServer s;
		try {
			s = new BasicServer(InetAddress.getLocalHost());
			s.checkIn();
			s.startTableWatchers();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startTableWatchers() {
		dbWatcher = new DatabaseWatcher();

		TaxiTableWatcher taxiWatcher = new TaxiTableWatcher(1000l);
		PrecosTableWatcher precosWatcher = new PrecosTableWatcher(1000l);
		ViagemTableWatcher viagemWatcher = new ViagemTableWatcher(1000l);
		StatusTableWatcher statusWatcher = new StatusTableWatcher(1000l);

		dbWatcher.registerTableWatcher(taxiWatcher);
		dbWatcher.registerTableWatcher(precosWatcher);
		dbWatcher.registerTableWatcher(viagemWatcher);
		dbWatcher.registerTableWatcher(statusWatcher);

		taxiWatcher.registerObserver(dbWatcher);
		precosWatcher.registerObserver(dbWatcher);
		viagemWatcher.registerObserver(dbWatcher);
		statusWatcher.registerObserver(dbWatcher);

		dbWatcher.registerObserver(this);

		dbWatcher.startWatchers();

		LOGGER.info("Exposing DatabaseWatcher to RMI.");
		binder.bindRemoteObject(DatabaseWatcher.REMOTE_NAME, dbWatcher);
	}

	public BasicServer(InetAddress localAddress) {
		localHost = localAddress;
		context = Context.getContext();
		context.setServerReference(this);
		LOGGER.info("Starting server: "
				+ context.getLocalAddress().getHostAddress());

	}

	@Override
	public void checkIn() {
		LOGGER.info("Starting checkIn procedure...");

		// FIXME SRP startShoutResponseListener
		// Inicia a o listener de ShoutResponse e envia um ShoutRequest
		ShoutResponse response = startShoutResponseListener();

		startShoutRequestListener();

		// Se ninguem respondeu o ShoutRequest..
		if (response.isEmpty()) {
			LOGGER.info("No one replyed shout message ;(");
		} else {
			// Se alguem respondeu o ShoutRequest...
			try {
				// Estabelece uma conexao RMI com o n� que respondeu o
				// ShoutRequest
				// Requisita o NetworkNodes deste servidor
				InetAddress networkNodeAddr = InetAddress
						.getByAddress(ShoutResponse
								.getShoutResponseIp(response));

				initRmiClient(networkNodeAddr);
				stabilishConnection();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Invariavelmente inicia um servidor RMI.
		// Se outro n� tentar se conectar a rede e quem responder for este n�,
		// ele ter� que ter um servidor RMI com NetworkNodes registrado para
		// enviar a ele
		initRmiServer();

	}

	private void initRmiClient(InetAddress networkNodeAddr) {
		rmiClient = new RmiClient();

		retriever = rmiClient.startClient(networkNodeAddr);
	}

	/**
	 * Obt�m uma inst�ncia de NetworkNodes de algum servidor existente.
	 */
	private void stabilishConnection() {
		networkServers = retriever.rerieve(TaxiServerNodes.REMOTE_NAME);

		try {
			networkServers.addNode(this);

			LOGGER.info("Total Network Nodes connected: "
					+ networkServers.size());
			// TODO ver maneira de fazer os nodes serem atualizados
			// quando um node se conectar a uma rede pre-existente, o
			// node ingressante ter� que se registrar como observer dos
			// nodes preexistentes e os preexistentes ter�o que se
			// registrar como observers do node ingressante
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initRmiServer() {
		networkServers = TaxiServerNodes.getInstance();
		try {
			networkServers.addNode(this);
			rmiServer = new RmiServer();
			LOGGER.info("Starting RMI Server...");
			binder = rmiServer.startServer();
			binder.bindRemoteObject(TaxiServerNodes.REMOTE_NAME, networkServers);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ShoutResponse startShoutResponseListener() {
		ShoutResponseListener shoutResponseListener = new ShoutResponseListener(
				context.getDefaultMulticastMessagesListenerInterval(),
				context.getUnicastPort());
		Thread shoutListener = new Thread(shoutResponseListener);
		shoutListener.start();
		// For�a a execu��o de shoutListener antes da chamada do metodo shout()
		TimerExecutor.sleepCurrentThread(500);

		shout();
		// Aguarda uma resposta para shout nos proximos 5s
		TimerExecutor.sleepCurrentThread(context
				.getDefaultShoutResponseTimeout());
		// Interrompe shoutListener. Se ninguem tiver respondido ao shout(),
		// este servidor � o �nico da rede.
		// Obs.: o ShoutRequestListenerTimer contina rodando para antender a
		// novas entradas na rede
		shoutListener.interrupt();

		return shoutResponseListener.getShoutResponse();
	}

	private void startShoutRequestListener() {
		LOGGER.trace("Starting Shout Request Listener.");
		Thread shoutListener = new Thread(new ShoutRequestListener(
				context.getDefaultMulticastMessagesListenerInterval(),
				context.getMulticastGroupAddress(), context.getLocalAddress()));
		shoutListener.start();

	}

	@Override
	public void shout() {
		LOGGER.trace("Sending Shout Request...");
		SocketMessageSender sender = new MulticastSocketMessageSender(
				context.getMulticastGroupAddress(), context.getMulticastPort());

		NetworkRequest shoutMessage = new ShoutRequest();

		sender.sendMessage(shoutMessage);
	}

	public static NoteElements<TaxiServer> getNetworkNodes() {
		return networkServers;
	}

	public static void setNetworkNodes(NoteElements<TaxiServer> networkNodes) {
		BasicServer.networkServers = networkNodes;
	}

	@Override
	public void startListeningTimers(long time) {

	}

	public BasicServer(BasicServer server) {
		this.binder = server.binder;
		this.context = server.context;
		this.localHost = server.localHost;
		this.retriever = server.retriever;
		this.rmiClient = server.rmiClient;
		this.rmiServer = server.rmiServer;
	}

	// gambi
	@Override
	public TaxiServer clone() {
		TaxiServer node = new BasicServer(this);
		return node;
	}

	@Override
	public void setNextNode(TaxiServer node) {
		this.nextNode = node;

	}

	@Override
	public void update(Set<DbTable> o) {
		if (updatedTables == null)
			updatedTables = new ArrayList<>();

		for (DbTable table : o) {
			updatedTables.add(table);
		}

	}

	@Override
	public TaxiServer getNextNode() {
		// TODO Auto-generated method stub
		return nextNode;
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerObserver(ServerObserver obs) {
		if (serverObservers == null)
			serverObservers = new ArrayList<>();

		serverObservers.add(obs);

	}

	@Override
	public void update(ServerObservable subject) {

	}

}
