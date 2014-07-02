package com.taxifleet.server.dao;

import java.io.Serializable;
import java.util.Set;

import com.taxifleet.server.dao.tables.DbTable;

public interface DBObserver extends Serializable {

	public void update(Set<DbTable> o);

}
