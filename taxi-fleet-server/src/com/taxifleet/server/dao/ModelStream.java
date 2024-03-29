package com.taxifleet.server.dao;

import com.taxifleet.server.dao.tables.DbTable;

public class ModelStream<T extends DbTable> {
	private T model;

	public ModelStream() {

	}

	public ModelStream(T t) {
		this.model = t;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public synchronized T peekData() {
		T aux = this.model;
		model = null;
		return aux;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(this instanceof ModelStream))
			return false;

		ModelStream<DbTable> temp = (ModelStream<DbTable>) o;

		if (this.peekData() == null && temp.peekData() == null) {
			if (!(this.peekData().equals(temp.peekData())))
				return false;
		}
		return true;
	}
}