package com.taxifleet.server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.Precos;

public class PrecosTableWatcher extends TableWatcher<Precos> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8481442208813948121L;
	private ModelStream<Precos> outdatedData;
	private List<TableObserver> observers;
	private static final String QUERY = "SELECT * FROM dbo.Precos WHERE dbo.Precos.atualizado = "
			+ SqlConstants.OUTDATED_BIT.value();
	public static final String TABLE_NAME = "Precos";

	private static Logger LOGGER = LogManager
			.getLogger(PrecosTableWatcher.class);

	public PrecosTableWatcher(long watchInterval) {
		super(watchInterval);
	}

	@Override
	public Precos getOutdatedData() {
		// TODO Auto-generated method stub
		return outdatedData.peekData();
	}

	@Override
	public void watch(ResultSet rs) throws SQLException {
		String placa = rs.getString("Placa");
		float valorKm = rs.getFloat("ValorKm");
		int atualizado = rs.getInt("Atualizado");

		ModelStream<Precos> temp = new ModelStream<Precos>(new Precos(placa,
				valorKm, atualizado));

		// primeiro acesso..
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
