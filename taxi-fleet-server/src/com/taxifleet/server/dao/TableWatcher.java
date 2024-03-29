package com.taxifleet.server.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taxifleet.server.dao.tables.DbTable;

public abstract class TableWatcher<T extends DbTable> implements Runnable,
		TableObservable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 733457524647370984L;

	private static final Logger LOGGER = LogManager
			.getLogger(TableWatcher.class);

	private long watchInterval;

	protected ResultSet rs;

	public static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=TaxiAdministrator;integratedSecurity=true;";

	public TableWatcher(long watchInterval) {
		this.watchInterval = watchInterval;
	}

	public abstract T getOutdatedData();

	public abstract String getSqlStatement();

	public abstract String getWatchetTableName();

	public abstract void watch(ResultSet rs) throws SQLException;

	@Override
	public void run() {
		while (true) {

			try (Connection con = DriverManager.getConnection(JDBC_URL);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(getSqlStatement());) {

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				LOGGER.trace("Watching table " + getWatchetTableName());
				synchronized (this) {
					while (rs.next()) {
						watch(rs);
					}
				}
			}

			// Handle any errors that may have occurred.
			catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(watchInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
