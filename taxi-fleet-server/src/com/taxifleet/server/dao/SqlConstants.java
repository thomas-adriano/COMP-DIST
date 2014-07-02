package com.taxifleet.server.dao;

public enum SqlConstants {

	OUTDATED_BIT(1), UP_TO_DATE_BIT(0);
	
	int bit;
	
	SqlConstants(int bit) {
		this.bit = bit;
	}
	
	public int value() {
		return bit;
	}
}
