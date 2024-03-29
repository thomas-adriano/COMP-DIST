package com.taxifleet.server.dao;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.DbTable;

public class DatabaseWatcher implements Remote, TableObserver, DBObservable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -801645259885966317L;
	private Set<DbTable> observedTables;
	private List<DBObserver> observers;
	private List<TableWatcher<? extends DbTable>> watchers;
	public static final String REMOTE_NAME = "DB_WATCHER";
	private static final Logger LOGGER = LogManager
			.getLogger(DatabaseWatcher.class);

	public void startWatchers() {
		if (watchers == null)
			throw new RuntimeException(
					"No watchers registered. Use registerWatcher()");

		for (TableWatcher<? extends DbTable> watcher : watchers) {
			Thread t = new Thread(watcher);
			t.start();
		}
	}

	public void registerTableWatcher(TableWatcher<? extends DbTable> watcher) {
		if (watchers == null)
			watchers = new ArrayList<>();

		watchers.add(watcher);
	}

	@Override
	public void registerObserver(DBObserver o) {
		if (observers == null)
			observers = new ArrayList<>();

		observers.add(o);

	}

	@Override
	public void notifyObservers() {
		LOGGER.trace("Notifying Server about database updates. Total DB updates: "+observedTables.size());
		for (DBObserver o : observers) {
			o.update(observedTables);
		}

	}

	@Override
	public void update(DbTable table) {
		Object lock = new Object();
		synchronized (lock) {
			if (observedTables == null)
				observedTables = new LinkedHashSet<>();

			observedTables.add(table);
		}
		notifyObservers();
	}

}
