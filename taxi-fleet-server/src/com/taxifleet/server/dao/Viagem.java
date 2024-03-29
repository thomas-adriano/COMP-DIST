package com.taxifleet.server.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "viagem")
public class Viagem {

	@Column(name = "placa")
	private String placa;
	@Column(name = "latitude")
	private float latitude;
	@Column(name = "longitude")
	private float longitude;
	@Column(name = "atualizado")
	private int atualizado;

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
