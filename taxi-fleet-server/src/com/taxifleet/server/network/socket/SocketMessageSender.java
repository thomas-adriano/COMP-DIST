package com.taxifleet.server.network.socket;

import com.taxifleet.server.network.NetworkRequest;

/**
 * Classe responsável por enviar mensagens socket pela network;
 * @author Thomas
 *
 */
public interface SocketMessageSender {

	public void sendMessage(NetworkRequest message);
	
}
