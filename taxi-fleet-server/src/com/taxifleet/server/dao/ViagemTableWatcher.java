package com.taxifleet.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.Viagem;

public class ViagemTableWatcher extends TableWatcher<Viagem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(ViagemTableWatcher.class);
	private ModelStream<Viagem> outdatedData;
	private List<TableObserver> observers;
	private static final String QUERY = "SELECT * FROM dbo.VIAGEM WHERE dbo.VIAGEM.atualizado = "
			+ SqlConstants.OUTDATED_BIT.value();
	public static final String TABLE_NAME = "Viagem";

	public ViagemTableWatcher(long watchInterval) {
		super(watchInterval);
	}

	@Override
	public Viagem getOutdatedData() {
		// TODO Auto-generated method stub
		return outdatedData.peekData();
	}

	@Override
	public void watch(ResultSet rs) throws SQLException {

		String placa = rs.getString("Placa");
		float latitude = rs.getFloat("latitude");
		float longitude = rs.getFloat("longitude");
		int atualizado = rs.getInt("atualizado");

		ModelStream<Viagem> temp = new ModelStream<Viagem>(new Viagem(placa, latitude,
				longitude, atualizado));
		
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
