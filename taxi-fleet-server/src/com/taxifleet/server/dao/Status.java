package com.taxifleet.server.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status {
	@Column(name = "placa")
	private String placa;
	@Column(name = "latitude")
	private float latitude;
	@Column(name = "longitude")
	private float longitude;
	@Column(name = "disponibilidade")
	private int disponibilidade;
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
