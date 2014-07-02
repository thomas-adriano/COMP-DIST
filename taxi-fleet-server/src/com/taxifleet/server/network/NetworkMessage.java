package com.taxifleet.server.network;

import com.taxifleet.utils.ByteArrayUtils;

public abstract class NetworkMessage {

	public static final byte HEADER_BODY_SEPARATOR = ':';

	public static final byte BODY_SEPARATOR = ',';

	public abstract byte[] getMessageHeader();

	public abstract int getMessageSize();
	
	public abstract byte[] toBytes();

	/**
	 * Checks the type of the message data in byte[] format.
	 * 
	 * @param bytes
	 *            message data
	 * @return true if the message data is of the same type of this
	 *         NetworkMessage, false otherwise
	 */
	public boolean checkType(byte[] bytes) {
		byte[] header = new byte[getMessageHeader().length];

		for (int i = 0; i < bytes.length; i++) {
			if ((char) bytes[i] == HEADER_BODY_SEPARATOR) {
				break;
			}

			header[i] = bytes[i];

		}

		return ByteArrayUtils.compareArrays(header, getMessageHeader());
	}

}
