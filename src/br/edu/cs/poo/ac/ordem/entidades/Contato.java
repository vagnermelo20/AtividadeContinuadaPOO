package br.edu.cs.poo.ac.ordem.entidades;

import java.io.Serializable;

public class Contato implements Serializable {
	
	private String email;
	private String celular;
	private boolean ehZap;

	public Contato(String email, String celular, boolean ehZap) {
		this.email = email;
		this.celular = celular;
		this.ehZap = ehZap;
	}

	public String getEmail() {
		return email;
	}
	public String getCelular() {
		return celular;
	}
	public boolean isEhZap() {
		return ehZap;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public void setEhZap(boolean ehZap) {
		this.ehZap = ehZap;
	}
}

//teste de commit