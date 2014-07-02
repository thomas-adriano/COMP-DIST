package com.taxifleet.server.network.socket;

import com.taxifleet.server.network.NetworkRequest;

/**
 * Classe respons�vel por enviar mensagens socket pela network;
 * @author Thomas
 *
 */
public interface SocketMessageSender {

	public void sendMessage(NetworkRequest message);
	
}
