package com.taxifleet.server.dao.tables;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status implements DbTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3245871611104567873L;
	private String placa;
	private float latitude;
	private float longitude;
	private int disponibilidade;
	private int atualizado;

	public Status(String placa, float latitude, float longitude,
			int disponibilidade, int atualizado) {
		this.placa = placa;
		this.latitude = latitude;
		this.longitude = longitude;
		this.disponibilidade = disponibilidade;
		this.atualizado = atualizado;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(int disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public int getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(int atualizado) {
		this.atualizado = atualizado;
	}

}
