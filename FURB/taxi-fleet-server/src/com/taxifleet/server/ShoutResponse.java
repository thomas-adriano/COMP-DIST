package com.taxifleet.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.taxifleet.server.network.NetworkMessage;
import com.taxifleet.server.network.NetworkResponse;
import com.taxifleet.utils.ByteArrayUtils;

public class ShoutResponse extends NetworkResponse {

	public static final byte[] HEADER = "SHOUT".getBytes();

	public ShoutResponse(byte[] data) {
		super(data);
	}

	public ShoutResponse() {

	}

	public ShoutResponse(byte[] bodyPart, byte[]... bodyParts) {
		super(mountMessageData(HEADER, bodyPart));

		int bodyPartsLengthsSum = 0;
		int numberOfBodyParts = 0;
		boolean hasBodyParts = false;

		if (bodyParts.length > 0) {
			hasBodyParts = true;
			numberOfBodyParts = 1;
			// soma o tamanho de todas as partes do corpo;
			// soma a quantidade de byte[] enviados (para inserir os devidos
			// separadores entre eles);
			for (byte[] bytes : bodyParts) {
				bodyPartsLengthsSum += bytes.length;
				numberOfBodyParts++;
			}
			numberOfBodyParts--; // retira o count do ultimo bodyPart para que
									// não haja um separador de body a mais no
									// final do body
		}
		byte[] bodyBuffer = new byte[bodyPart.length + bodyPartsLengthsSum
				+ numberOfBodyParts];

		// apesar do construtor da superclasse já setar (parte d)o body, refaz
		// aqui para poder ajustar com os outros bodyParts
		int pos = 0;
		for (byte each : bodyPart) {
			bodyBuffer[pos] = each;
			pos++;
		}

		if (hasBodyParts) {

			bodyBuffer[pos++] = BODY_SEPARATOR;

			for (int i = 0; i < bodyParts.length; i++) {
				for (byte bytes : bodyParts[i]) {
					bodyBuffer[pos] = bytes;
					pos++;
				}
				if (i != bodyParts.length - 1) {
					bodyBuffer[pos] = BODY_SEPARATOR;
					pos++;
				}
			}
		}
		body = new byte[bodyBuffer.length];
		ByteArrayUtils.copyByteArray(bodyBuffer, body);
	}

	@Override
	public byte[] getMessageBody() {
		return body;
	}

	@Override
	public byte[] toBytes() {
		return mountMessageData(HEADER, body);
	}

	public boolean isEmpty() {
		if (body == null)
			return true;

		if (body.length <= 0)
			return true;

		return false;
	}

	@Override
	public byte[] getMessageHeader() {
		return HEADER;
	}

	@Override
	public int getMessageSize() {
		return toBytes().length;
	}

	/**
	 * Extracts the IP data from the shout message
	 * 
	 * @param msg
	 *            A ShoutResponse to extract the IP data
	 * @return the IP data from the ShoutResponse parameter
	 */
	public static byte[] getShoutResponseIp(ShoutResponse msg) {
		byte[] buffer = msg.getMessageBody();
		byte[] preResult = new byte[buffer.length];
		int pos = 0;
		for (byte each : buffer) {
			if ((char) each == NetworkMessage.BODY_SEPARATOR) {
				break;
			}
			preResult[pos] = buffer[pos];
			pos++;
		}
		byte[] finalResult = new byte[pos];
		ByteArrayUtils.copyByteArrayIgnoringLengthDifference(preResult,
				finalResult);
		return finalResult;
	}

	/**
	 * Returns the port number data from the ShoutResponse parameter
	 * 
	 * @param msg
	 *            ShoutResponse to have the port number extracted
	 * @return port number data of the ShoutResponse parameter
	 */
	public static int getShoutResponsePort(ShoutResponse msg) {
		byte[] buffer = msg.getMessageBody();
		byte[] preResult = new byte[buffer.length];
		int pos = 0;
		boolean isPortData = false;
		for (int i = 0; i < buffer.length; i++) {
			if ((char) buffer[i] == NetworkMessage.BODY_SEPARATOR) {
				isPortData = true;
				i++; // avanca a posicao do bodySeparator
			}

			if (isPortData) {
				preResult[pos] = buffer[i];
				pos++;
			}
		}
		byte[] finalResult = new byte[pos];
		ByteArrayUtils.copyByteArrayIgnoringLengthDifference(preResult,
				finalResult);
		return ByteArrayUtils.byteArrayToInt(finalResult);
	}

}
