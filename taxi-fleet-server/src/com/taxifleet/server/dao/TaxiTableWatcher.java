package com.taxifleet.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.Taxi;

public class TaxiTableWatcher extends TableWatcher<Taxi> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201757870407555350L;
	private ModelStream<Taxi> outdatedData;
	private List<TableObserver> observers;
	private static final String QUERY = "SELECT * FROM dbo.TAXI WHERE dbo.TAXI.atualizado = "
			+ SqlConstants.OUTDATED_BIT.value();
	public static final String TABLE_NAME = "Taxi";
	private static Logger LOGGER = LogManager.getLogger(TaxiTableWatcher.class);

	public TaxiTableWatcher(long watchInterval) {
		super(watchInterval);
	}

	@Override
	public Taxi getOutdatedData() {
		return outdatedData.peekData();
	}

	@Override
	public void watch(ResultSet rs) throws SQLException {
		String placa = rs.getString("Placa");
		String modelo = rs.getString("Modelo");
		String marca = rs.getString("Marca");
		int atualizado = rs.getInt("atualizado");

		ModelStream<Taxi> temp = new ModelStream<Taxi>(new Taxi(placa, modelo,
				marca, atualizado));
		
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
