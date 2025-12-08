package br.edu.cs.poo.ac.ordem.entidades;

import java.time.LocalDate;
import java.time.Period;
import br.edu.cs.poo.ac.utils.Registro;

public class Cliente implements Registro {
	private String cpfCnpj; 
	private String nome;
	private Contato contato;
	private LocalDate dataCadastro;
	
	public Cliente(String cpfCnpj, String nome, Contato contato, LocalDate dataCadastro) {
		
		this.cpfCnpj = cpfCnpj;
		this.nome = nome;
		this.contato = contato;
		this.dataCadastro = dataCadastro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
	public int getIdadeCadastro() {
		
		Period IdadeTotal = Period.between(dataCadastro, LocalDate.now());
		int Idade = IdadeTotal.getYears();
		return Idade;
		
	}
	public String getId() {
		return this.cpfCnpj;
	}
	
	
}
