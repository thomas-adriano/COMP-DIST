package com.taxifleet.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.Status;

public class StatusTableWatcher extends TableWatcher<Status> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9011079914693109053L;

	private ModelStream<Status> outdatedData;
	private List<TableObserver> observers;
	private static final String QUERY = "SELECT * FROM dbo.Status WHERE dbo.Status.atualizado = "
			+ SqlConstants.OUTDATED_BIT.value();
	public static final String TABLE_NAME = "Status";
	private static Logger LOGGER = LogManager
			.getLogger(StatusTableWatcher.class);

	public StatusTableWatcher(long watchInterval) {
		super(watchInterval);
		outdatedData = new ModelStream<>();
	}

	@Override
	public Status getOutdatedData() {
		// TODO Auto-generated method stub
		return outdatedData.peekData();
	}

	@Override
	public void watch(ResultSet rs) throws SQLException {
		String placa = rs.getString("Placa");
		float latitude = rs.getFloat("Latitude");
		float longitude = rs.getFloat("Longitude");
		int disponibilidade = rs.getInt("Disponibilidade");
		int atualizado = rs.getInt("Atualizado");

		ModelStream<Status> temp = new ModelStream<Status>(new Status(placa,
				latitude, longitude, disponibilidade, atualizado));
		
		//primeiro acesso..
		if (outdatedData == null) {
			outdatedData = temp;
		} else {
			if (!outdatedData.equals(temp)) {
				outdatedData = temp;
				notifyObservers();
			}
		}

	}

	@Override
	public String getSqlStatement() {
		return QUERY;
	}

	@Override
	public String getWatchetTableName() {
		return TABLE_NAME;
	}

	@Override
	public void registerObserver(TableObserver o) {
		if (observers == null)
			observers = new ArrayList<>();

		observers.add(o);
	}

	@Override
	public void notifyObservers() {
		LOGGER.trace("Table " + getWatchetTableName()
				+ " update detected. Notifying observers...");
		for (TableObserver o : observers) {
			o.update(outdatedData.peekData());
		}
	}
}
