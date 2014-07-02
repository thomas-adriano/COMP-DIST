package com.taxifleet.server.network.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.ShoutResponse;
import com.taxifleet.utils.ByteArrayUtils;

public class ShoutResponseListener extends Listener {

	private int port;
	private ShoutResponse shoutResponse;
	private ServerSocket server;
	private static final Logger LOGGER = LogManager
			.getLogger(ShoutResponseListener.class);

	public ShoutResponseListener(long time, int port) {
		super(time);
		this.port = port;
		try {
			this.server = new ServerSocket(this.port);
			
			InetAddress localhost = InetAddress.getLocalHost();

			LOGGER.trace("Starting Shout Response Listener");
			LOGGER.debug("At address: " + localhost.getHostAddress() + ":"
					+ port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ShoutResponse getShoutResponse() {
		return (shoutResponse == null ? new ShoutResponse()
				: shoutResponse);
	}

	@Override
	public void listen() throws InterruptedException {
		synchronized (this) {
			try {
				LOGGER.debug("Shout Response Listener started.");
				Socket socket = server.accept();
				Iterator<Byte> bufferIterator = null;// TODO nullpointer aqui..
				try (DataInputStream inStream = new DataInputStream(
						socket.getInputStream());) {

					bufferIterator = ByteArrayUtils
							.readBytesFromIputStream(inStream);
				}

				shoutResponse = new ShoutResponse(
						ByteArrayUtils
								.byteIteratorToPrimiteveByteArray(bufferIterator));

				LOGGER.trace("Shout message replyed.");

				System.err.println("--Conteudo de shoutResponse--");
				ByteArrayUtils.printByteArrayContent(shoutResponse.toBytes());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
