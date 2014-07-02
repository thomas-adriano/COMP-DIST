package com.taxifleet.server.dao;

import com.taxifleet.server.dao.tables.DbTable;

public interface TableObserver {

	public void update(DbTable table);
	
}
