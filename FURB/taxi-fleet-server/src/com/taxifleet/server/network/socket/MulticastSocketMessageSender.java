package com.taxifleet.server.network.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.network.NetworkRequest;

/**
 * Envia mensagens Multicast via socket.
 * 
 * @author Thomas
 *
 */
public class MulticastSocketMessageSender implements SocketMessageSender {

	private InetAddress mCastGroup;
	private int port;
	private static final Logger LOGGER = LogManager
			.getLogger(MulticastSocketMessageSender.class);

	public MulticastSocketMessageSender(InetAddress multicastNetAddressGroup,
			int port) {
		this.mCastGroup = multicastNetAddressGroup;
		this.port = port;
	}

	public InetAddress getMulticastGroupAddr() {
		return this.mCastGroup;
	}

	/**
	 * Envia uma mensagem mutlicast para os participantes do grupo registrado no
	 * construtor.
	 * 
	 * @param message
	 */
	@Override
	public void sendMessage(NetworkRequest message) {
		try (DatagramSocket socket = new DatagramSocket();) {
			byte[] msgData = message.toBytes();
			DatagramPacket dgram = new DatagramPacket(msgData, msgData.length,
					mCastGroup, port);

			LOGGER.debug("Sending multicast message.");
			LOGGER.debug("Data: " + new String(msgData));
			LOGGER.debug("Destination: " + dgram.getAddress() + ':'
					+ dgram.getPort());

			socket.send(dgram);
		} catch (IOException ex) {
			// TODO AUTO GENERATED CATCH
			ex.printStackTrace();
		}

	}
}
