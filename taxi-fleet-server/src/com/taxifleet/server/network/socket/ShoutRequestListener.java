package com.taxifleet.server.network.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.ShoutRequest;
import com.taxifleet.server.ShoutResponse;
import com.taxifleet.server.Context;
import com.taxifleet.server.network.NetworkResponse;
import com.taxifleet.utils.ByteArrayUtils;

/**
 * Timer de escuta de mensagens ShoutMesage
 * 
 * @author Thomas
 *
 */
public class ShoutRequestListener extends Listener {

	private InetAddress mCastGroup;
	private InetAddress serverAddress;
	private int multicastPort;
	private int unicastPort;
	private ShoutResponse shoutResponse;
	private ShoutRequest shoutRequest;
	private Context context = com.taxifleet.server.Context.getContext();
	private static final Logger LOGGER = LogManager
			.getLogger(ShoutRequestListener.class);

	public ShoutRequestListener(long listeningInterval,
			InetAddress multicastNetAddressGroup,
			InetAddress serverNetAddress) {
		super(listeningInterval);
		this.mCastGroup = multicastNetAddressGroup;
		this.serverAddress = serverNetAddress;
		this.multicastPort = context.getMulticastPort();
		this.unicastPort = context.getUnicastPort();
		
		// TODO Esta verificacao deve ser melhorada.. est� estranha...
		// instanciacao s� � feita para se usar o checkType abaixo
		this.shoutResponse = new ShoutResponse();
		this.shoutRequest = new ShoutRequest();
		LOGGER.info("Starting (Multicast) Shout Request Listener at "
				+ mCastGroup.getHostAddress() + ":" + multicastPort);

	}

	@Override
	public void listen() throws InterruptedException {
		byte[] emptyBuffer = new byte[shoutRequest.getMessageSize()];

		DatagramPacket packet = new DatagramPacket(emptyBuffer,
				emptyBuffer.length);

		try (MulticastSocket mCastSocket = new MulticastSocket(multicastPort);) {
			mCastSocket.joinGroup(mCastGroup);

			LOGGER.info("Shout Request Listener started.");
			
			mCastSocket.receive(packet); // escuta no grupo vinculado at�
											// receber alguma mensagem

			LOGGER.debug("Packet received from " + packet.getAddress());

			// verifica se o conteudo do packet � um shoutMessage.
			if (shoutRequest.checkType(packet.getData())) {

				InetAddress shouterAddress = packet.getAddress(); // netAddress
				// do
				// respons�vel
				// pela
				// mensagem
				// multicast
				LOGGER.debug("Shouter's IP: "
						+ packet.getAddress().getHostAddress());

				// pela mensagem
				// multicast

				LOGGER.debug("Shouter's port (default multicast port): "
								+ unicastPort);
				
				try (Socket socket = new Socket(shouterAddress,
						unicastPort);) {
					
					//TODO isso deveria estar sendo feito dentro de ShoutResponse...
					byte[] dataToSend = NetworkResponse.mountMessageData(shoutRequest.getMessageHeader(), serverAddress.getAddress());
					
					shoutResponse = new ShoutResponse(dataToSend);
					
					LOGGER.debug("Replying Shout Request to (shouter): "
									+ shouterAddress.getHostAddress() + ":"
									+ unicastPort);
					
					byte[] replyData = shoutResponse.toBytes();
					
					LOGGER.debug("---Begin reply data---");
					ByteArrayUtils.printByteArrayContent(replyData);
					LOGGER.debug("---End reply data---");
					
					try (DataOutputStream outStream = new DataOutputStream(
							socket.getOutputStream());) {
						outStream
								.writeBytes(new String(replyData));
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}

			packet.setLength(shoutRequest.getMessageSize()); // reseta packet
																// length

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
