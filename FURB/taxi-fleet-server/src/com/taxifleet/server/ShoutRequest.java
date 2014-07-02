package com.taxifleet.server;

import com.taxifleet.server.network.NetworkRequest;

public class ShoutRequest extends NetworkRequest {

	public static final byte[] HEADER = "SHOUT".getBytes();

	@Override
	public byte[] getMessageHeader() {
		return HEADER;
	}

	@Override
	public int getMessageSize() {
		return HEADER.length;
	}

	/**
	 * the byte[] form of this message is only its HEADER info
	 */
	@Override
	public byte[] toBytes() {
		return HEADER;
	}

}
