package com.taxifleet.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.utils.ConfigFileHandler;

public class Context implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695092319350009563L;
	private InetAddress localAddress;
	private InetAddress multicastGroupAddress;
	private int multicastPort;
	private int unicastPort;
	private int defaultRmiPort = 1099;
	private InetAddress startingNodeAddr;
	private long defaultMulticastMessagesListenerInterval = 1000l;
	private long defaultShoutResponseTimeout = 3000l;
	private static final Logger LOGGER = LogManager.getLogger(Context.class);
	private BasicServer serverReference;

	private static Context singletonContext;

	private Context() {

	}

	
	
	public BasicServer getServerReference() {
		return serverReference;
	}



	public void setServerReference(BasicServer serverReference) {
		this.serverReference = serverReference;
	}



	public static Context getContext() {
		if (singletonContext == null) {
			LOGGER.info("Starting up Context...");
			singletonContext = new Context();
			singletonContext.load();
		}
		return singletonContext;
	}

	public void load() {
		LOGGER.trace("Loading Context...");
		try {
			this.localAddress = InetAddress.getLocalHost();
			LOGGER.debug("Local address: " + this.localAddress.getHostAddress());
			this.multicastGroupAddress = InetAddress
					.getByName(ConfigFileHandler
							.getProperty(ConfigFileHandler.MULTICAST_ADDRESS_KEY));
			LOGGER.debug("Multicast group address: "
					+ this.multicastGroupAddress.getHostAddress());

			try {
				this.multicastPort = Integer.valueOf(ConfigFileHandler
						.getProperty(ConfigFileHandler.MULTICAST_PORT_KEY));
				LOGGER.debug("Multicast port: " + this.multicastPort);
				this.unicastPort = Integer.valueOf(ConfigFileHandler
						.getProperty(ConfigFileHandler.UNICAST_PORT_KEY));
				LOGGER.debug("Unicast port: " + this.unicastPort);

				String mcastListenerInterval = ConfigFileHandler
						.getProperty(ConfigFileHandler.MULTICAST_MESSAGES_LISTENER_INTERVAL);
				defaultMulticastMessagesListenerInterval = mcastListenerInterval
						.isEmpty() == true ? defaultMulticastMessagesListenerInterval
						: Long.parseLong(mcastListenerInterval);
				LOGGER.debug("Multicast Listener Interval: "
						+ this.defaultMulticastMessagesListenerInterval
						+ " millis");

				String shoutResponseTimeout = ConfigFileHandler
						.getProperty(ConfigFileHandler.SHOUT_RESPONSE_TIMEOUT);
				defaultShoutResponseTimeout = shoutResponseTimeout.isEmpty() == true ? defaultShoutResponseTimeout
						: Long.parseLong(shoutResponseTimeout);
				LOGGER.debug("Shout Response Timeout: "
						+ this.defaultShoutResponseTimeout + " millis");

				String rmiPort = ConfigFileHandler
						.getProperty(ConfigFileHandler.RMI_PORT);
				defaultRmiPort = rmiPort.isEmpty() == true ? defaultRmiPort
						: Integer.parseInt(rmiPort);
				LOGGER.debug("RMI Port: " + this.defaultRmiPort);
			} catch (NumberFormatException ex) {
				LOGGER.error(
						"Could not convert number value from properties file.",
						ex);
				ex.printStackTrace();
			}
		} catch (UnknownHostException e) {
			LOGGER.fatal("Fail loading context.", e);
			e.printStackTrace();
		}
	}

	public int getDefaultRmiPort() {
		return defaultRmiPort;
	}

	public void setDefaultRmiPort(int defaultRmiPort) {
		this.defaultRmiPort = defaultRmiPort;
	}

	public long getDefaultShoutResponseTimeout() {
		return defaultShoutResponseTimeout;
	}

	public void setDefaultShoutResponseTimeout(long defaultShoutResponseTimeout) {
		this.defaultShoutResponseTimeout = defaultShoutResponseTimeout;
	}

	public InetAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public InetAddress getMulticastGroupAddress() {
		return multicastGroupAddress;
	}

	public void setMulticastGroupAddress(InetAddress multicastGroupAddress) {
		this.multicastGroupAddress = multicastGroupAddress;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public int getUnicastPort() {
		return unicastPort;
	}

	public void setUnicastPort(int unicastPort) {
		this.unicastPort = unicastPort;
	}

	public InetAddress getStartingNodeAddr() {
		return startingNodeAddr;
	}

	public void setStartingNodeAddr(InetAddress startingNodeAddr) {
		this.startingNodeAddr = startingNodeAddr;
	}

	public long getDefaultMulticastMessagesListenerInterval() {
		return defaultMulticastMessagesListenerInterval;
	}

	public void setDefaultMulticastMessagesListenerInterval(
			long defaultMulticastMessagesListenerInterval) {
		this.defaultMulticastMessagesListenerInterval = defaultMulticastMessagesListenerInterval;
	}

}
