package com.taxifleet.server.dao.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "taxi")
public class Taxi implements DbTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4669422262174909628L;
	@Column(name = "placa")
	private String placa;
	@Column(name = "modelo")
	private String modelo;
	private String marca;
	@Column(name = "atualizado")
	private int atualizado;

	public Taxi(String placa, String modelo, String marca, int atualizado) {
		this.placa = placa;
		this.modelo = modelo;
		this.marca = marca;
		this.atualizado = atualizado;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public int getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(int atualizado) {
		this.atualizado = atualizado;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

}
