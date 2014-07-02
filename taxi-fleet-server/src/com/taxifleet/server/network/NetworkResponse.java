package com.taxifleet.server.network;


public abstract class NetworkResponse extends NetworkMessage {

	public abstract byte[] getMessageBody();
	protected byte[] body = new byte[0];
	
	public NetworkResponse() {
		
	}
	
	public NetworkResponse(byte[] data) {
		if (!checkType(data)) {
			throw new IllegalArgumentException(
					"O tipo dado enviado não é válido");
		}

		int pos = getMessageHeader().length + 1; // inicia a posição após o header +
										// headerbodyseparator
		body = new byte[data.length - getMessageHeader().length - 1]; // body deve ter o
															// tamanho igual a
															// data - header -
															// headerseparator
		for (int i = pos; i < data.length; i++) {
			int bodyPos = i - pos;
			body[bodyPos] = data[i];
		}
	}
	
	public static byte[] mountMessageData(byte[] header, byte[] body) {
		byte[] result = new byte[header.length + body.length + 1];

		for (int i = 0; i < result.length;) {
			for (int j = 0; j < header.length; j++, i++) {
				result[i] = header[j];
			}

			result[i++] = HEADER_BODY_SEPARATOR;

			for (int k = 0; k < body.length; k++, i++) {
				result[i] = body[k];
			}
		}

		return result;
	}
	
}
