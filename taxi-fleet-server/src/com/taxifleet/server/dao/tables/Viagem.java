package com.taxifleet.server.dao.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "viagem")
public class Viagem implements DbTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "placa")
	private String placa;
	@Column(name = "latitude")
	private float latitude;
	@Column(name = "longitude")
	private float longitude;
	@Column(name = "atualizado")
	private int atualizado;

	public Viagem(String placa, float latitude, float longitude, int atualizado) {
		this.placa = placa;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public int getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(int atualizado) {
		this.atualizado = atualizado;
	}

}
