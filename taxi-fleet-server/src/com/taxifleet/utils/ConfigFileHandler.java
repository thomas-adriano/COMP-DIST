package com.taxifleet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileHandler {

	public static final String MULTICAST_ADDRESS_KEY = "multicast.address";
	public static final String MULTICAST_PORT_KEY = "multicast.port";
	public static final String UNICAST_PORT_KEY = "unicast.port";
	public static final String SHOUT_RESPONSE_TIMEOUT = "shout.response.timeout";
	public static final String MULTICAST_MESSAGES_LISTENER_INTERVAL = "multicast.messages.listener.interval";
	public static final String RMI_PORT = "rmi.port";
	
	public static final File PROPERTIES_FILE = new File("resources/config.properties");
	
	public static String getProperty(String property) {
		Properties prop = new Properties();
		try (FileInputStream fileStream = new FileInputStream(PROPERTIES_FILE)) {
			prop.load(fileStream);
			return (String) prop.get(property);
		} catch (IOException ex) {
			//TODO ERROR LOG
		}
		return new String("");
	}
	
}
