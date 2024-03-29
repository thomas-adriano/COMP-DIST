package com.taxifleet.server.dao.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Precos")
public class Precos implements DbTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1254485828806051468L;

	@Column(name = "placa")
	private String placa;

	@Column(name = "valorKm")
	private float valorKm;

	@Column(name = "Atualizado")
	private int atualizado;

	
	public Precos(String placa, float valorKm, int atualizado) {
		this.placa = placa;
		this.valorKm = valorKm;
		this.atualizado = atualizado;
	}
	
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public float getValorKm() {
		return valorKm;
	}

	public void setValorKm(float valorKm) {
		this.valorKm = valorKm;
	}

	public int getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(int atualizado) {
		this.atualizado = atualizado;
	}


}
