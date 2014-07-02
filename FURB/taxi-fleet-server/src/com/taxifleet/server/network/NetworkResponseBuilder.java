package com.taxifleet.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.ShoutResponse;

public class NetworkResponseBuilder {

	private static final Logger LOGGER = LogManager
			.getLogger(NetworkResponseBuilder.class);

	public static NetworkResponse createResponse(byte[] data) {
		LOGGER.debug("Creating Network Response throught Builder...");
		NetworkResponse shout = new ShoutResponse();
		if (shout.checkType(data)) {
			LOGGER.debug("Shout Response header found. Creating Shout Response Response.");
			return shout;
		}

		throw new IllegalArgumentException(
				"This header is not supported by any message.");
	}
}
