package com.taxifleet.server.network.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.network.NetworkRequest;
import com.taxifleet.server.network.NetworkResponse;
import com.taxifleet.server.network.NetworkResponseBuilder;

public class UnicastSocketMessageSender implements SocketMessageSender {

	private static final Logger LOGGER = LogManager
			.getFormatterLogger(UnicastSocketMessageSender.class);
	private InetAddress serverAddress;
	private int port;

	public UnicastSocketMessageSender(InetAddress serverAddress, int port) {
		this.serverAddress = serverAddress;
		this.port = port;
	}

	@Override
	public void sendMessage(NetworkRequest message) {
		LOGGER.info("IP destino da mensagem: " + message.getClass().getName()
				+ ": " + serverAddress.getHostAddress());

		LOGGER.debug("Porta utilizada para envio da mensagem shout: " + port);

		try (Socket socket = new Socket(serverAddress, port);) {
			LOGGER.debug("Enviando resposta à endereço: "
					+ serverAddress.getHostAddress() + ":" + port);
			// monta a networkMessage de resposta
			NetworkResponse response = NetworkResponseBuilder
					.createResponse(serverAddress.getAddress());

			try (ObjectOutputStream outStream = new ObjectOutputStream(
					socket.getOutputStream());) {
				outStream.writeObject(response);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
